package com.AiBlog.Blogger.ScrapperModule.model;

import com.AiBlog.Blogger.ScrapperModule.Enums.Category;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "blogs")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Lob
    private String content;

    private LocalDate publishDate;


    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDate getPublishDate() { return publishDate; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }

}

