package com.petya136900.rcebot.vk.structures;

import com.google.gson.annotations.SerializedName;
import com.petya136900.rcebot.tools.JsonParser;

public class Button {
	
	private ButtonAction action;
	private String color;
	
	public Button(Type type) {
		this.setAction(new ButtonAction(type));
	}
	public Button(Type type, String label) {
		this.setAction(new ButtonAction(type,label));
	}
	public Button(Type type, String label, String color) {
		this.setAction(new ButtonAction(type,label));
		setColor(color);
	}
	public Type getType() {
		return action.getType();
	}
	public String getLabel() {
		return action.getLabel();
	}
	public Button setLabel(String label) {
		action.setLabel(label);
		return this;
	}
	public <T> T getPayload(Class<T> class1) {
		return JsonParser.fromJson(this.getAction().getPayload(), class1);
	}
	public Button setPayload(Object payload) {
		action.setPayload(JsonParser.toJson(payload));
		return this;
	}
	public String getLink() {
		return action.getLink();
	}
	public Button setLink(String link) {
		action.setLink(link);
		return this;
	}
	public String getHash() {
		return action.getHash();
	}
	public Button setHash(String hash) {
		action.setHash(hash);
		return this;
	}
	public Integer getApp_id() {
		return action.getApp_id();
	}
	public Button setApp_id(Integer app_id) {
		action.setApp_id(app_id);
		return this;
	}
	public Integer getOwner_id() {
		return action.getOwner_id();
	}
	public Button setOwner_id(Integer owner_id) {
		action.setOwner_id(owner_id);
		return this;
	}
	public String getColor() {
		return color;
	}
	public Button setColor(String color) {
		if((action.getType().equals(Type.CALLBACK))|(action.getType().equals(Type.TEXT))) {
			this.color = checkColor(color);
		}
		return this;
	}
	private String checkColor(String color) {
		color = color.toLowerCase().trim();
		switch(color) {
		case(COLOR_POSITIVE):
		case(COLOR_NEGATIVE):
		case(COLOR_PRIMARY):
		case(COLOR_SECONDARY):
			return color;
		default:
			throw new IllegalArgumentException("Bad color");
		}
	}
	public ButtonAction getAction() {
		return action;
	}
	public void setAction(ButtonAction action) {
		this.action = action;
	}

	public enum Type {
		@SerializedName("text")
		TEXT("text"),
		@SerializedName("open_link")
		OPEN_LINK("open_link"),
		@SerializedName("vkpay")
		VKPAY("vkpay"),
		@SerializedName("open_app")
		VKAPPS("open_app"),
		@SerializedName("callback")
		CALLBACK("callback"),
		@SerializedName("location")
		LOCATION("location");
		private final String type;
		Type(final String type) {
			this.type = type;
		}
		@Override
		public String toString() {
		    return type.toLowerCase();
		}
		public static Type getType(String type) {
			switch(type.toLowerCase()) {
				case("text"):
					return Type.TEXT;
				case("open_link"):
					return Type.OPEN_LINK;
				case("vkpay"):
					return Type.VKPAY;
				case("open_app"):
					return Type.VKAPPS;
				case("callback"):
					return Type.CALLBACK;
				case("location"):
					return Type.LOCATION;
				default:
					throw new IllegalArgumentException("Bad type");
			}
		}
	}
	public static final String COLOR_PRIMARY="primary";
	public static final String COLOR_SECONDARY="secondary";
	public static final String COLOR_NEGATIVE="negative";
	public static final String COLOR_POSITIVE="positive";

	/**
	 * Don't serialize Payload value
	 */
	public Button setPayload(String payload, boolean donotmatter) {
		if(payload.length()>255) {
			throw new IllegalArgumentException("Payload should be not more than 255 letters length");
		}
		if(!JsonParser.isJson(payload)) {
			throw new IllegalArgumentException("Payload must be in JSON-format");
		}
		action.setPayload(payload);
		return this;
	}
	/**
	 * @return Not serialized Payload value
	 */
	public String payload(boolean donotmatter) {
		return action.getPayload();
	}
}
