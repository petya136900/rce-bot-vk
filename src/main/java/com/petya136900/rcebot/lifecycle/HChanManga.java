package com.petya136900.rcebot.lifecycle;

// #content
public class HChanManga { // >.content_row
    private String link; // >.manga_images > HCHAN_SCHEME + HCHAN_URL + a[href] (/manga/ or /games/)
    private String thumbImgUrl; // >img[href] .replaceFirst("_blur","");
    private String coverUrl; // >> link > #cover[src] // maybe empty
    private String readLink; // >> link > #manga_images > a[href] // maybe empty
    private String[] pageUrls; // >> readLink > #thumbs > img[src] (replace _thumbs "")
    private String title; // .manga_row1 >title_link .text()
    // delete .manga_row3[0] (delete .row3_right)
    private String itemName; // .manga_row3[0] .item
    private String itemContent; // > .item2 .text()
    private String itemLink; // > .item2 >a[0][href]
    private String[] tags; // .manga_row3[1] .item4 .text() .split(",") .forEach(trim())
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getThumbImgUrl() {
        return thumbImgUrl;
    }
    public void setThumbImgUrl(String thumbImgUrl) {
        this.thumbImgUrl = thumbImgUrl;
    }
    public String getCoverUrl() {
        return coverUrl;
    }
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
    public String getReadLink() {
        return readLink;
    }
    public void setReadLink(String readLink) {
        this.readLink = readLink;
    }
    public String[] getPageUrls() {
        return pageUrls;
    }
    public void setPageUrls(String[] pageUrls) {
        this.pageUrls = pageUrls;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getItemContent() {
        return itemContent;
    }
    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }
    public String getItemLink() {
        return itemLink;
    }
    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }
    public String[] getTags() {
        return tags;
    }
    public void setTags(String[] tags) {
        this.tags = tags;
    }
    // if itemName == Тип ignore it
}
