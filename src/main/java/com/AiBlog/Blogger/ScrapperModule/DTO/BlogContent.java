package com.AiBlog.Blogger.ScrapperModule.DTO;
import com.AiBlog.Blogger.ScrapperModule.Enums.Category;

import java.util.List;

public class BlogContent {
    private final String content;
    private final String title;
    private final List<String> sourceUrls;
    private final Category category;

    public BlogContent(String title, String content, List<String> sourceUrls, Category category) {
        this.title = title;
        this.content = content;
        this.sourceUrls = sourceUrls;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public List<String> getSourceUrls() {
        return sourceUrls;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

