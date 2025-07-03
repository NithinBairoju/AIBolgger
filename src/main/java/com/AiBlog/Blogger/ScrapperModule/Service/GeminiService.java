package com.AiBlog.Blogger.ScrapperModule.Service;

import com.AiBlog.Blogger.ScrapperModule.DTO.BlogContent;
import com.AiBlog.Blogger.ScrapperModule.Enums.Category;
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
import java.util.stream.Collectors;

@Service
public class GeminiService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);
    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiService() {
        this.restTemplate = new RestTemplate();
    }

    public BlogContent generateBlog(List<ArticleMetaData> articles, Category category) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are writing a UPSC blog for category: ").append(category.name()).append(".\n");
        prompt.append("Here are some article headlines and links. Based on these headlines, select the top 3–5 most important for UPSC preparation. ");
        prompt.append("Then write a comprehensive summary of each selected article with proper analysis for UPSC aspirants. ");
        prompt.append("Include the source links at the end.\n\n");
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
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + apiKey;

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> responseBody = response.getBody();
            String blogMarkdown = extractBlogText(responseBody);

            return new BlogContent(
                    "Top News Summary for " + category.name(),
                    blogMarkdown, // Now contains Markdown instead of HTML
                    articles.stream().map(ArticleMetaData::getUrl).collect(Collectors.toList()),
                    category
            );

        } catch (Exception e) {
            logger.error("Gemini API call failed for category {}: {}", category.name(), e.getMessage(), e);

            return new BlogContent(
                    "[FAILED] Top News Summary for " + category.name(),
                    "<p>Failed to generate blog content. Error: " + e.getMessage() + "</p>",
                    articles.stream().map(ArticleMetaData::getUrl).collect(Collectors.toList()),
                    category
            );
        }
    }

    /**
     * Extract the blog text from Gemini API response
     * Fixed the complex nested map extraction with proper type safety
     */
    private String extractBlogText(Map<String, Object> responseBody) {
        try {
            if (responseBody == null) {
                logger.warn("Response body is null from Gemini API");
                return "<p>No response received from Gemini API</p>";
            }

            // Navigate through the response structure safely with proper type checking
            Object candidatesObj = responseBody.get("candidates");
            if (!(candidatesObj instanceof List)) {
                logger.warn("No candidates found in Gemini API response");
                return "<p>No content generated from Gemini API</p>";
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) candidatesObj;

            if (candidates.isEmpty()) {
                logger.warn("Empty candidates list in Gemini API response");
                return "<p>No content generated from Gemini API</p>";
            }

            Map<String, Object> firstCandidate = candidates.getFirst();
            Object contentObj = firstCandidate.get("content");

            if (!(contentObj instanceof Map)) {
                logger.warn("No content found in first candidate");
                return "<p>No content in API response</p>";
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> content = (Map<String, Object>) contentObj;
            Object partsObj = content.get("parts");

            if (!(partsObj instanceof List)) {
                logger.warn("No parts found in content");
                return "<p>No parts in API response</p>";
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> parts = (List<Map<String, Object>>) partsObj;

            if (parts.isEmpty()) {
                logger.warn("Empty parts list in API response");
                return "<p>No parts in API response</p>";
            }

            Map<String, Object> firstPart = parts.get(0);
            Object textObj = firstPart.get("text");

            if (!(textObj instanceof String)) {
                logger.warn("No text found in first part");
                return "<p>No text in API response</p>";
            }

            String text = (String) textObj;

            if (text.trim().isEmpty()) {
                logger.warn("Empty text in API response");
                return "<p>Empty text in API response</p>";
            }

            logger.info("Successfully extracted blog markdown of length: {}", text.length());
            return text.trim(); // Return raw Markdown - no HTML conversion needed

        } catch (ClassCastException e) {
            logger.error("Type casting error while extracting blog text: {}", e.getMessage());
            return "<p>Error parsing API response: Invalid response format</p>";
        } catch (Exception e) {
            logger.error("Unexpected error extracting blog text: {}", e.getMessage(), e);
            return "<p>Error parsing API response: " + e.getMessage() + "</p>";
        }
    }


}