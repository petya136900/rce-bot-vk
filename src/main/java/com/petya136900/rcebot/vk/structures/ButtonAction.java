package com.petya136900.rcebot.vk.structures;

import com.petya136900.rcebot.vk.structures.Button.Type;

public class ButtonAction {
	public ButtonAction(Type type) {
		this.type=type;
	}
	public ButtonAction(Type type, String label) {
		this.type=type;
		setLabel(label);
	}
	private Type type;
	private String label;
	private String payload;
	private String link;
	private String hash;
	private Integer app_id;
	private Integer owner_id;
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getLabel() {
		
		return label;
	}
	public void setLabel(String label) {
		if((!(type.equals(Type.VKPAY)))&(!(type.equals(Type.LOCATION)))) {
			this.label=label;
		}
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		if(type.equals(Type.OPEN_LINK)) {
			this.link = link;
		}
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		if((type.equals(Type.VKAPPS))|(type.equals(Type.VKPAY))) {
			this.hash = hash;
		}
	}
	public Integer getApp_id() {
		return app_id;
	}
	public void setApp_id(Integer app_id) {
		if(type.equals(Type.VKAPPS)) {
			this.app_id = app_id;
		}
	}
	public Integer getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(Integer owner_id) {
		if(type.equals(Type.VKAPPS)) {
			this.owner_id = owner_id;
		}
	}
}
