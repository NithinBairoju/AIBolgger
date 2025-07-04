package com.AiBlog.Blogger.ScrapperModule.queue;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class BlogTaskQueue {
    private final BlockingQueue<BlogGenerationTask> queue = new LinkedBlockingQueue<>();

    public void publish(BlogGenerationTask task) {
        queue.offer(task);
    }

    public BlogGenerationTask take() throws InterruptedException {
        return queue.take(); // blocks until task is available
    }
}
