package com.AiBlog.Blogger.ScrapperModule.rss;

import com.AiBlog.Blogger.ScrapperModule.Enums.Category;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RssFeedProvider {
    public Map<Category,String> getCategoryFeeds() {
        return Map.of(
                Category.POLITY, "https://feeds.feedburner.com/ndtvnews-india-news",
                Category.WORLD, "https://feeds.feedburner.com/ndtvnews-world-news",
                Category.ECONOMY, "https://feeds.feedburner.com/ndtvprofit-latest",
                Category.SCIENCE, "https://feeds.feedburner.com/gadgets360-latest",
                Category.HEALTH, "https://feeds.feedburner.com/ndtvcooks-latest",
                Category.SOCIETY, "https://feeds.feedburner.com/ndtvnews-people"
        );
    }
}
