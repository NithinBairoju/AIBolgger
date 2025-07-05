package com.AiBlog.Blogger.shared.Repository;


import com.AiBlog.Blogger.shared.Enums.Category;
import com.AiBlog.Blogger.shared.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findByCategoryOrderByPublishDateDesc(Category category);
}
