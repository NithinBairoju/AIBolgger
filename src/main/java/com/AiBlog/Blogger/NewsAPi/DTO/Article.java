package com.AiBlog.Blogger.NewsAPi.DTO;


import com.AiBlog.Blogger.shared.Enums.Category;
import java.time.LocalDateTime;

public class Article {
    private Long id;
    private String content;
    private Category category;
    private LocalDateTime publishDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime  getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime  publishDate) {
        this.publishDate = publishDate;
    }
}
