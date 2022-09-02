package com.petya136900.rcebot.other.hchan;

import com.petya136900.rcebot.tools.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;

// #content
public class HChanManga { // >.content_row
    private String link; // >.manga_images > HCHAN_SCHEME + HCHAN_URL + a[href] (/manga/ or /games/)
    private String thumbImgUrl; // >img[href] .replaceFirst("_blur","");
    private String coverUrl; // >> link > #cover[src] // maybe empty
    private String readLink; // >> link > #manga_images > a[href] // maybe empty
    private String[] pageUrls; // >> readLink > #thumbs > img[src] (replace _thumbs "")
    private String title; // .manga_row1 >title_link .text()
    private String description; // .tags .text()
    // delete .manga_row3[0] (delete .row3_right)
    private String itemName; // .manga_row3[0] .item
    private String itemContent; // > .item2 .text()
    private String itemLink; // > .item2 >a[0][href]
    private String[] tags; // .manga_row3[1] .item4 .text() .split(",") .forEach(trim())
    private boolean existInDB = false;
    private boolean parsed = false;
    private String[] attachs;
    private String coverAttach; //
    public HChanManga() {

    }
    public HChanManga(HChanSQLObject sqlObject) {
        if(sqlObject.isExist()) {
            existInDB = true;
        } else return;
        if(sqlObject.isParsed())
            this.parsed = true;
        setLink(sqlObject.getLink());
        this.attachs = JsonParser.fromJson(sqlObject.getAttaches(),String[].class);
        ArrayList<String> al = new ArrayList<>();
        Arrays.stream(sqlObject.getTags().split(" ")).forEach(x->al.add(x));
        this.tags = al.toArray(new String[al.size()]);
        HChanManga hcFB = JsonParser.fromJson(sqlObject.getJsonData(), HChanManga.class);
        setCoverUrl(hcFB.getCoverUrl());
        setItemName(hcFB.getItemName());
        setItemContent(hcFB.getItemContent());
        setItemLink(hcFB.getItemLink());
        setCoverAttach(hcFB.getCoverAttach());
        setParsed(hcFB.isParsed());
        setDescription(hcFB.getDescription());
        setPageUrls(hcFB.getPageUrls());
        setTitle(hcFB.getTitle());
        setReadLink(hcFB.getReadLink());
        setThumbImgUrl(hcFB.getThumbImgUrl());
    }
    public String getLink() {
        return link;
    }
    public HChanManga setLink(String link) {
        this.link = link;
        return this;
    }
    public String getThumbImgUrl() {
        return thumbImgUrl;
    }
    public HChanManga setThumbImgUrl(String thumbImgUrl) {
        this.thumbImgUrl = thumbImgUrl;
        return this;
    }
    public String getCoverUrl() {
        return coverUrl;
    }
    public HChanManga setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
        return this;
    }
    public String getReadLink() {
        return readLink;
    }
    public HChanManga setReadLink(String readLink) {
        this.readLink = readLink;
        return this;
    }
    public String[] getPageUrls() {
        return pageUrls;
    }
    public HChanManga setPageUrls(String[] pageUrls) {
        this.pageUrls = pageUrls;
        return this;
    }
    public String getTitle() {
        return title;
    }
    public HChanManga setTitle(String title) {
        this.title = title;
        return this;
    }
    public String getItemName() {
        return itemName;
    }
    public HChanManga setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }
    public String getItemContent() {
        return itemContent;
    }
    public HChanManga setItemContent(String itemContent) {
        this.itemContent = itemContent;
        return this;
    }
    public String getItemLink() {
        return itemLink;
    }
    public HChanManga setItemLink(String itemLink) {
        this.itemLink = itemLink;
        return this;
    }
    public String[] getTags() {
        return tags;
    }
    public HChanManga setTags(String[] tags) {
        this.tags = tags;
        return this;
    }
    public boolean existInDB() {
        return existInDB;
    }
    public HChanManga setExistInDB(boolean existInDB) {
        this.existInDB = existInDB;
        return this;
    }
    public String[] getAttachs() {
        return attachs;
    }
    public HChanManga setAttachs(String[] attachs) {
        this.attachs = attachs;
        return this;
    }
    public String getDescription() {
        return description;
    }
    public HChanManga setDescription(String description) {
        this.description = description;
        return this;
    }
    public String getCoverAttach() {
        return coverAttach;
    }
    public HChanManga setCoverAttach(String coverAttach) {
        this.coverAttach = coverAttach;
        return this;
    }
    public boolean isParsed() {
        return parsed;
    }
    public HChanManga setParsed(boolean parsed) {
        this.parsed = parsed;
        return this;
    }
}
