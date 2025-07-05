package com.AiBlog.Blogger.ScrapperModule.Service;

import com.AiBlog.Blogger.ScrapperModule.DTO.BlogContent;
import com.AiBlog.Blogger.shared.Repository.BlogRepository;
import com.AiBlog.Blogger.shared.model.Blog;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
public class BlogWriterService {
    private final BlogRepository blogRepository;

    public BlogWriterService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public void saveBlog(BlogContent blogContent) {
        Blog blog = new Blog();
        blog.setContent(blogContent.getContent());
        blog.setCategory(blogContent.getCategory());
        blog.setPublishDate(LocalDateTime.now());
        blogRepository.save(blog);
    }
}