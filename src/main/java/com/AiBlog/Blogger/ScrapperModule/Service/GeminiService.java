package com.AiBlog.Blogger.ScrapperModule.Service;

import com.AiBlog.Blogger.ScrapperModule.DTO.BlogContent;
import com.AiBlog.Blogger.ScrapperModule.DataCleaning.ResponseCleaning;
import com.AiBlog.Blogger.shared.Enums.Category;
import com.AiBlog.Blogger.ScrapperModule.queue.ArticleMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);
    private final RestTemplate restTemplate;
    private final ResponseCleaning responseCleaning;

    public GeminiService(ResponseCleaning responseCleaning) {
        this.responseCleaning = responseCleaning;
        this.restTemplate = new RestTemplate();
    }

    /**
     * API key for Gemini API, injected from application properties
     */
    @Value("${gemini.api.key}")
    private String apiKey;



    public BlogContent generateBlog(List<ArticleMetaData> articles, Category category) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are writing a UPSC blog for category: ").append(category.name()).append(".\n");
        prompt.append("Here are some article headlines and links. Based on these headlines, select the top 3–5 most important for UPSC preparation. ");
        prompt.append("Then write a comprehensive summary of each selected article with proper analysis for UPSC aspirants. write title according to thenews in the blog. ");
        prompt.append("Include the source links at the end of each news .\n\n");
        prompt.append("**IMPORTANT**: Format your response in proper Markdown format with:\n");
        prompt.append("- Use ## for main headings\n");
        prompt.append("- Use ### for subheadings\n");
        prompt.append("- Use **bold** for important terms\n");
        prompt.append("- Use bullet points with - for lists\n");
        prompt.append("- Use [link text](URL) for source links\n");
        prompt.append("- Structure the content with clear sections\n\n");

        int index = 1;
        for (ArticleMetaData article : articles) {
            prompt.append(index++).append(". Title: ").append(article.getName()).append("\n");
            prompt.append("   URL: ").append(article.getUrl()).append("\n\n");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request body according to Gemini API format
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(
                Map.of("parts", List.of(Map.of("text", prompt.toString())))
        ));

        // Add generation configuration for better output
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.7);
        generationConfig.put("maxOutputTokens", 2048);
        generationConfig.put("topP", 0.95);
        generationConfig.put("topK", 40);
        requestBody.put("generationConfig", generationConfig);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // Use the correct API endpoint with API key as parameter
        String apiUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> responseBody = response.getBody();
            String blogMarkdown = responseCleaning.extractBlogText(responseBody);
            String cleanedMarkdown = responseCleaning.cleanAnalysisSections(blogMarkdown);

            return new BlogContent(
                    blogMarkdown, // Now contains Markdown instead of HTML
                    category
            );

        } catch (Exception e) {
            logger.error("Gemini API call failed for category {}: {}", category.name(), e.getMessage(), e);

            return new BlogContent(
                    "<p>Failed to generate blog content. Error: " + e.getMessage() + "</p>",
                    category
            );
        }
    }

}