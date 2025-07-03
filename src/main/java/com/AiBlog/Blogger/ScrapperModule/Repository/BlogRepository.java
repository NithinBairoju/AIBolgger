package com.AiBlog.Blogger.ScrapperModule.Repository;


import com.AiBlog.Blogger.ScrapperModule.Enums.Category;
import com.AiBlog.Blogger.ScrapperModule.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

}
