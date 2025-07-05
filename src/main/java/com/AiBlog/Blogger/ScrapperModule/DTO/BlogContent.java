package com.AiBlog.Blogger.ScrapperModule.DTO;
import com.AiBlog.Blogger.shared.Enums.Category;

public class BlogContent {
    private final String content;
    private final Category category;

    public BlogContent( String content, Category category) {
        this.content = content;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }
}

