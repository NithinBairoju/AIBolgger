package com.AiBlog.Blogger.ScrapperModule.Service;

import com.AiBlog.Blogger.ScrapperModule.DTO.BlogContent;
import com.AiBlog.Blogger.ScrapperModule.Repository.BlogRepository;
import com.AiBlog.Blogger.ScrapperModule.model.Blog;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BlogWriterService {
    private final BlogRepository blogRepository;

    public BlogWriterService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public void saveBlog(BlogContent blogContent) {
        Blog blog = new Blog();
        blog.setTitle(blogContent.getTitle());
        blog.setContent(blogContent.getContent());
        blog.setSourceUrls(blogContent.getSourceUrls());
        blog.setCategory(blogContent.getCategory());
        blog.setPublishDate(LocalDate.now());
        blogRepository.save(blog);
    }
}