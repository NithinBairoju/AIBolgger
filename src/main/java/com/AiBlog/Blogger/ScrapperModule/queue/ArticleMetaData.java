package com.AiBlog.Blogger.ScrapperModule.queue;

public class ArticleMetaData {
    private String Name;
    private String url;

    public ArticleMetaData(String name, String url) {
        Name = name;
        this.url = url;
    }

    public ArticleMetaData() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
