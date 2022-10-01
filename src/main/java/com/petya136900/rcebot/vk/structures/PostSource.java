package com.petya136900.rcebot.vk.structures;

public class PostSource {
    private Type type;
    private Platform platform;
    private Data data;
    private String url;
    public enum Type {
        vk,
        widget,
        api,
        rss,
        sms
    }
    public enum Platform {
        android,
        iphone,
        wphone
    }
    public enum Data {
        profile_activity,
        profile_photo,
        comments,
        like,
        poll
    }
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public Platform getPlatform() {
        return platform;
    }
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
