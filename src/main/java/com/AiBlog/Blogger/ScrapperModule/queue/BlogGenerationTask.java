package com.AiBlog.Blogger.ScrapperModule.queue;

import com.AiBlog.Blogger.ScrapperModule.Enums.Category;
import org.springframework.stereotype.Component;

import java.util.List;


public class BlogGenerationTask {
    private final Category category;
    private final List<ArticleMetaData> articles;

    public BlogGenerationTask(Category category, List<ArticleMetaData> articles) {
        this.category = category;
        this.articles = articles;
    }

    public Category getCategory() {
        return category;
    }

    public List<ArticleMetaData> getArticles() {
        return articles;
    }
}
