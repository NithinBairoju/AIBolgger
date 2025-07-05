package com.AiBlog.Blogger.NewsAPi.Mapper;

import com.AiBlog.Blogger.NewsAPi.DTO.Article;
import com.AiBlog.Blogger.shared.model.Blog;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {
    public Article blogToArticle(Blog blog) {
        if (blog == null) {
            return null;
        }
        Article article = new Article();
        article.setId(blog.getId());
        article.setContent(blog.getContent());
        article.setCategory(blog.getCategory());
        article.setPublishDate(blog.getPublishDate());
        return article;
    }
}
