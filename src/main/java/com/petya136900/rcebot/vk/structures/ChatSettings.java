package com.petya136900.rcebot.vk.structures;

public class ChatSettings {
    private Integer members_count;
    private String title;
    private VKMessage pinned_message;
    private String state;
    private VKAttachment.Photo photo;
    private boolean is_group_channel;
    private Integer[] active_ids;
    public Integer getMembers_count() {
        return members_count;
    }
    public void setMembers_count(Integer members_count) {
        this.members_count = members_count;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public VKMessage getPinned_message() {
        return pinned_message;
    }
    public void setPinned_message(VKMessage pinned_message) {
        this.pinned_message = pinned_message;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public VKAttachment.Photo getPhoto() {
        return photo;
    }
    public void setPhoto(VKAttachment.Photo photo) {
        this.photo = photo;
    }
    public boolean isIs_group_channel() {
        return is_group_channel;
    }
    public void setIs_group_channel(boolean is_group_channel) {
        this.is_group_channel = is_group_channel;
    }
    public Integer[] getActive_ids() {
        return active_ids;
    }
    public void setActive_ids(Integer[] active_ids) {
        this.active_ids = active_ids;
    }
}
