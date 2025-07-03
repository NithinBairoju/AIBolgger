package com.AiBlog.Blogger.ScrapperModule.Service;

import com.AiBlog.Blogger.ScrapperModule.Enums.Category;
import com.AiBlog.Blogger.ScrapperModule.queue.ArticleMetaData;
import com.AiBlog.Blogger.ScrapperModule.queue.BlogGenerationTask;
import com.AiBlog.Blogger.ScrapperModule.queue.BlogTaskQueue;
import com.AiBlog.Blogger.ScrapperModule.rss.RssFeedProvider;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RssFetcherService {
    private final RssFeedProvider provider;
    private final BlogTaskQueue taskQueue;

    public RssFetcherService(RssFeedProvider provider, BlogTaskQueue taskQueue) {
        this.provider = provider;
        this.taskQueue = taskQueue;
    }
    public void fetchFeedsAndQueueTasks() {
        Map<Category, String> feeds = provider.getCategoryFeeds();

        feeds.forEach((category, feedUrl) -> {
            try {
                URL url = new URL(feedUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(inputStream);
                doc.getDocumentElement().normalize();

                NodeList items = doc.getElementsByTagName("item");
                List<ArticleMetaData> articles = new ArrayList<>();

                for (int i = 0; i < Math.min(10, items.getLength()); i++) {
                    Node node = items.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String title = cleanData(element.getElementsByTagName("title").item(0).getTextContent());
                        String link = cleanData(element.getElementsByTagName("link").item(0).getTextContent());
                        articles.add(new ArticleMetaData(title.trim(), link.trim()));
                    }
                }

                if (!articles.isEmpty()) {
                    BlogGenerationTask task = new BlogGenerationTask(category, articles);
                    taskQueue.addTask(task);
                }
            } catch (Exception e) {
                System.err.println("Failed to fetch RSS feed for category " + category + ": " + e.getMessage());
            }
        });
    }
    private String cleanData(String content) {
        if (content == null) return "";

        // Remove CDATA wrapper
        content = content.replaceAll("<!\\[CDATA\\[", "").replaceAll("\\]\\]>", "");

        // Remove any remaining XML artifacts
        content = content.replaceAll("<[^>]*>", "");

        // Clean up extra whitespace
        content = content.replaceAll("\\s+", " ");

        return content.trim();
    }
}
