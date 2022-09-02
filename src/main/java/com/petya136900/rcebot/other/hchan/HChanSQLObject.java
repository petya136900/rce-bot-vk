package com.petya136900.rcebot.other.hchan;

import com.petya136900.rcebot.tools.JsonParser;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HChanSQLObject {
    private Long ID;
    private final String link;
    private String jsonData;
    private String attaches;
    private boolean parsed=false; // 0
    private String tags;
    private boolean exist=false;

    public HChanSQLObject(Long id, String link, Boolean parsed, String attaches, String jsonData, String tags) {
        this.ID=id;
        this.link=link;
        this.parsed=parsed;
        this.attaches = attaches;
        this.jsonData=jsonData;
        this.tags=tags;
    }

    public Long getID() {
        return ID;
    }
    public void setID(Long ID) {
        this.ID = ID;
    }
    public String getLink() {
        return link;
    }
    public String getJsonData() {
        return jsonData;
    }
    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
    public String getAttaches() {
        return attaches;
    }
    public void setAttaches(String attaches) {
        this.attaches = attaches;
    }
    public boolean isParsed() {
        return parsed;
    }
    public void setParsed(boolean parsed) {
        this.parsed = parsed;
    }
    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public HChanSQLObject(HChanManga comic) {
        this.link = comic.getLink();
        if(comic.getAttachs()!=null) {
            this.attaches = JsonParser.toJson(comic.getAttachs());
        } else {
            this.attaches = JsonParser.toJson(new String[]{});
        }
        this.jsonData = JsonParser.toJson(new HChanManga()
                .setCoverUrl(comic.getCoverUrl())
                .setItemName(comic.getItemName())
                .setItemContent(comic.getItemContent())
                .setItemLink(comic.getItemLink())
                .setDescription(comic.getDescription())
                .setPageUrls(comic.getPageUrls())
                .setParsed(comic.isParsed())
                .setCoverAttach(comic.getCoverAttach())
                .setTitle(comic.getTitle())
                .setReadLink(comic.getReadLink())
                .setThumbImgUrl(comic.getThumbImgUrl()));
        this.tags = Arrays.stream(comic.getTags()).collect(Collectors.joining(" "));
    }

    public void setExist(boolean b) {
        this.exist=b;
    }
    public boolean isExist() {
        return this.exist;
    }
}
