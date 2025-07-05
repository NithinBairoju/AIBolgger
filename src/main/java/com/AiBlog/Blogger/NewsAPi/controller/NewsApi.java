package com.AiBlog.Blogger.NewsAPi.controller;

import com.AiBlog.Blogger.NewsAPi.DTO.Article;
import com.AiBlog.Blogger.NewsAPi.Service.ArticleService;
import com.AiBlog.Blogger.shared.Enums.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/blogs/v1")
public class NewsApi {
    private final ArticleService articleService;
    public NewsApi(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/blogs")
    public  ResponseEntity<List<Article>> getAllArticles(){
        return ResponseEntity.ok(articleService.getAllArticles());
    }
    @GetMapping("/blog/{id}")
    public  ResponseEntity<Article> getArticleById(@PathVariable Long id){
        return ResponseEntity.ok(articleService.getArticleById(id));
    }
    @GetMapping("category/{category}")
    public ResponseEntity<List<Article>>getArticlesByCategory(@PathVariable Category category) {
        return ResponseEntity.ok(articleService.getArticlesByCategory(category));
    }
}
