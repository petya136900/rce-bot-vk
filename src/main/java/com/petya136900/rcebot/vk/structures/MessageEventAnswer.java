package com.petya136900.rcebot.vk.structures;

public class MessageEventAnswer {
	private Integer peer_id;
	private Integer user_id;
	private String event_id;
	private EventData event_data;
	public MessageEventAnswer(Integer peer_id, Integer user_id, String event_id, EventData event_data) {
		this.peer_id=peer_id;
		this.user_id=user_id;
		this.event_id=event_id;
		this.event_data=event_data;
	}
	public Integer getPeer_id() {
		return peer_id;
	}
	public void setPeer_id(Integer peer_id) {
		this.peer_id = peer_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public EventData getEvent_data() {
		return event_data;
	}
	public void setEvent_data(EventData event_data) {
		this.event_data = event_data;
	}

}
