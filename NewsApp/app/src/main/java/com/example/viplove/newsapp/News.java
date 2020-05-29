package com.example.viplove.newsapp;

public class News {
    private String SectionName;
    private String webTitle;
    private String date;
    private String url;
    private String author;

    public News(String nSectionName, String nwebTitle, String ndate, String nurl,String mauthor) {
        SectionName = nSectionName;
        webTitle = nwebTitle;
        date = ndate;
        url = nurl;
        author=mauthor;
    }
    public News(String nSectionName, String nwebTitle, String ndate, String nurl) {
        SectionName = nSectionName;
        webTitle = nwebTitle;
        date = ndate;
        url = nurl;

    }


    public String getUrl() {
        return url;
    }

    public String getSectionName() {
        return SectionName;
    }


    public String getDate() {
        return date;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getAuthor() {
        return author;
    }
}