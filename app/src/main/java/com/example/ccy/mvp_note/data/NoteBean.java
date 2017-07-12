package com.example.ccy.mvp_note.data;

import java.util.UUID;

/**
 * Created by ccy on 2017-07-11.
 */

public class NoteBean {
    public String id;
    public String title;
    public String content;
    public boolean isActive;

    public NoteBean(String title, String content, boolean isActive) {
        this.id = UUID.randomUUID().toString();  //保证id唯一性
        this.title = title;
        this.content = content;
        this.isActive = isActive;
    }

    public NoteBean(String id, String title, String content, boolean isActive) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isActive = isActive;
    }
}
