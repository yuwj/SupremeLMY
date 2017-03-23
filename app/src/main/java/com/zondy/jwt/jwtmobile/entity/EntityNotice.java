package com.zondy.jwt.jwtmobile.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public class EntityNotice implements Serializable {

    public static final int NOTICE_TYPE_TONGZGG= 1;//通知公告
    public static final int NOTICE_TYPE_BUK= 2;//布控


    String id;
    String title;
    int type;
    String content;
    String time;
    List<String> filePaths;
    List<EntityFeedback> feedbacks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<EntityFeedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<EntityFeedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public List<String> getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

