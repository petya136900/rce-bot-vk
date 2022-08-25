package com.petya136900.rcebot.vk.structures;

public class VKObjectCB {
	private Integer user_id;
	private Integer peer_id;
	private String event_id;
	private String payload;
	private Integer conversation_message_id;
	public Integer getConversation_message_id() {
		return conversation_message_id;
	}
	public void setConversation_message_id(Integer conversation_message_id) {
		this.conversation_message_id = conversation_message_id;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
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
	public VKObject toVKObject() {
		Payload payloadO = new Payload();
		payloadO.setData(payload);
		return new VKObject(null,null,user_id,peer_id,event_id,payloadO,conversation_message_id==null?0:conversation_message_id);
	}
}
