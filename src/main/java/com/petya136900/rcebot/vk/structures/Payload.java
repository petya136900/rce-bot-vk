package com.petya136900.rcebot.vk.structures;

import com.petya136900.rcebot.tools.JsonParser;

public class Payload {
	private String handler;
	private String stage;
	private String data;
	private String command;
	private String button_type;
	private String payload;
    public Payload(String handler, String stage, String data) {
		this.handler=handler;
		this.stage=stage;
		this.data=data;
    }
    public boolean isUnsupportedCallback() {
		if(command!=null) {
			if(button_type!=null) {
				if(command.equals("not_supported_button")&button_type.equals("callback")) {
					return true;
				}
			}
		}
		return false;
	}
	public Payload getUnsupportedCallbackPayload() {
		if(payload!=null) {
			return JsonParser.fromJson(payload, Payload.class);
		}
		return null;
	}
	public Payload(String handler, String stage) {
		this.handler=handler;
		this.stage=stage;
	}
	public Payload(String handler) {
		this.handler=handler;
	}
	public Payload() {

	}
	public String getHandler() {
		return handler;
	}
	public Payload setHandler(String handler) {
		this.handler = handler;
		return this;
	}
	public String getStage() {
		return stage;
	}
	public Payload setStage(String stage) {
		this.stage = stage;
		return this;
	}
	public String getData() {
		return data;
	}
	public Payload setData(String data) {
		this.data = data;
		return this;
	}
}
