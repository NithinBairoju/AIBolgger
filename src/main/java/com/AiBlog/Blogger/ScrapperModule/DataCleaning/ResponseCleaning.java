package com.AiBlog.Blogger.ScrapperModule.DataCleaning;

import com.AiBlog.Blogger.ScrapperModule.Service.GeminiService;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

@Component
public class ResponseCleaning {
    /**
     * Extract the blog text from Gemini API response
     * Fixed the complex nested map extraction with proper type safety
     */

    private static final Logger logger = LoggerFactory.getLogger(ResponseCleaning.class);

    public String extractBlogText(Map<String, Object> responseBody) {
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
    public String cleanAnalysisSections(String markdown) {
        return markdown.replaceAll("(?s)\\*\\*Analysis:\\*\\*.*?(?=\\n\\n|$)", "").trim();
    }
}
