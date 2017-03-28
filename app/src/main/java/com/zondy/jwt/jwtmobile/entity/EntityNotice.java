package com.zondy.jwt.jwtmobile.entity;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public class EntityNotice implements Serializable {

    public static final int NOTICE_TYPE_TONGZGG = 1;//通知公告
    public static final int NOTICE_TYPE_BUK = 2;//布控


    String id;
    String title;
    @SerializedName("messageType")
    int type;
    @SerializedName("message")
    String content;
    @SerializedName("tssj")
    String time;
    @SerializedName("dmtlj")
    String mediaPaths;
    List<String> filePaths;//通知的多媒体信息
    List<EntityFeedback> feedbacks;//反馈信息集合.

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
        List<String> datas = new ArrayList<>();
        if (!TextUtils.isEmpty(mediaPaths)) {
            String[] d = mediaPaths.split(",");
            if (d != null && d.length > 0) {
                for (String s : d) {
                    datas.add(s);
                }
            }
        }
        return datas;
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

