package com.petya136900.rcebot.vk.structures;

import com.google.gson.annotations.SerializedName;

public class EventData {
	private Type type;
	public enum Type {
		@SerializedName("show_snackbar")
		SHOW_SNACKBAR,
		@SerializedName("open_link")
		OPEN_LINK,
		@SerializedName("open_app")
		OPEN_APP
	}
	private String text;
	public EventData(Type type, String text) {
		this.type=type;
		if(text.length()>90) {
			text = text.substring(0,90);
		}
		this.text=text;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Integer getApp_id() {
		return app_id;
	}
	public void setApp_id(Integer app_id) {
		this.app_id = app_id;
	}
	public Integer getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(Integer owner_id) {
		this.owner_id = owner_id;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	private String link;
	private Integer app_id;
	private Integer owner_id;
	private String hash;
}
