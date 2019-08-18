package com.easywetchat.model;

import java.io.Serializable;
import java.util.List;

public class FeedBean implements Serializable {

    public String avatarUrl;

    public String nickName;

    public String content;

    public List<String> images;

    public SenderInfo sender;

    public List<CommentInfo> comments;
}
