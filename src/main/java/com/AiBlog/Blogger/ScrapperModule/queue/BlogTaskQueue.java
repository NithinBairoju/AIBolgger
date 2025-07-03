package com.AiBlog.Blogger.ScrapperModule.queue;

import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class BlogTaskQueue {
    private final Queue<BlogGenerationTask> queue = new ConcurrentLinkedQueue<>();
    public void addTask(BlogGenerationTask task) {
        queue.add(task);
    }
    public BlogGenerationTask pollTask(){
        return queue.poll();
    }
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
