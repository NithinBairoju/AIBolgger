package com.AiBlog.Blogger.ScrapperModule.DTO;
import com.AiBlog.Blogger.ScrapperModule.Enums.Category;

import java.util.List;

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

