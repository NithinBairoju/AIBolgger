package com.AiBlog.Blogger.NewsAPi.Service;

import com.AiBlog.Blogger.NewsAPi.DTO.Article;
import com.AiBlog.Blogger.NewsAPi.Mapper.ArticleMapper;
import com.AiBlog.Blogger.shared.Enums.Category;
import com.AiBlog.Blogger.shared.Repository.BlogRepository;
import com.AiBlog.Blogger.shared.model.Blog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final BlogRepository blogRepository;
    private final ArticleMapper articleMapper;
    public ArticleService(BlogRepository blogRepository, ArticleMapper articleMapper) {
        this.blogRepository = blogRepository;
        this.articleMapper = articleMapper;
    }

    public List<Article> getAllArticles() {
        List<Blog> blogs =   blogRepository.findAll();
        return blogs.stream()
                .map(articleMapper::blogToArticle)
                .toList();
    }
    public List<Article> getArticlesByCategory(Category category){
        List<Blog> blogs = blogRepository.findByCategoryOrderByPublishDateDesc(category);
        return blogs.stream()
                .map(articleMapper::blogToArticle)
                .toList();
    }

    public Article getArticleById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        return articleMapper.blogToArticle(blog);
    }
}
