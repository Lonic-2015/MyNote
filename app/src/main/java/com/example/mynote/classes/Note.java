package com.example.mynote.classes;

/**
 * Created by 金晨 on 2016-06-02.
 */
public class Note {
    private String title="未命名";
    private String content;

    public Note() {
    }

    public Note(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
