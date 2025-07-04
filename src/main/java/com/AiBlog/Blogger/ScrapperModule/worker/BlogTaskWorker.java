package com.AiBlog.Blogger.ScrapperModule.worker;

import com.AiBlog.Blogger.ScrapperModule.Service.BlogWriterService;
import com.AiBlog.Blogger.ScrapperModule.Service.GeminiService;
import com.AiBlog.Blogger.ScrapperModule.queue.BlogGenerationTask;
import com.AiBlog.Blogger.ScrapperModule.queue.BlogTaskQueue;
import org.springframework.stereotype.Component;

@Component
public class BlogTaskWorker {

    private final BlogTaskQueue queue;
    private final GeminiService geminiService;
    private final BlogWriterService blogWriterService;

    public BlogTaskWorker(BlogTaskQueue queue,
                          GeminiService geminiService,
                          BlogWriterService blogWriterService) {
        this.queue = queue;
        this.geminiService = geminiService;
        this.blogWriterService = blogWriterService;

        // Start the listener thread
        Thread workerThread = new Thread(this::listen);
        workerThread.setDaemon(true);
        workerThread.start();
    }

    public void listen() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                BlogGenerationTask task = queue.take();
                var blog = geminiService.generateBlog(task.getArticles(), task.getCategory());
                blogWriterService.saveBlog(blog);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // reset interrupt flag
                System.err.println("Worker thread interrupted: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Worker error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

