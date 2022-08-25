package com.petya136900.rcebot.vk.structures;

public class VKObject {	
	private VKMessage message;
	private ClientInfo client_info;
	private Integer user_id;
	private Integer peer_id;
	private String event_id;
	private Payload payload;
	private Integer conversation_message_id;
	public VKObject() {
		//
	}
	public VKObject(VKMessage message, ClientInfo client_info, int user_id, int peer_id, String event_id,
			Payload payload, int conversation_message_id) {
		this.message=message;
		this.client_info=client_info;
		this.user_id=user_id;
		this.peer_id=peer_id;
		this.event_id=event_id;
		this.payload=payload;
		this.conversation_message_id=conversation_message_id;
	}
	public VKMessage getMessage() {
		return message;
	}
	public void setMessage(VKMessage message) {
		this.message = message;
	}
	public ClientInfo getClient_info() {
		return client_info;
	}
	public void setClient_info(ClientInfo client_info) {
		this.client_info = client_info;
	}
	/**
	 * only for message_event(callback)
	 */
	public Integer getConversation_message_id() {
		return conversation_message_id;
	}
	public void setConversation_message_id(Integer conversation_message_id) {
		this.conversation_message_id = conversation_message_id;
	}
	/**
	 * only for message_event(callback)
	 */
	public Payload getPayload() {
		return payload;
	}
	public void setPayload(Payload payload) {
		this.payload = payload;
	}
	/**
	 * only for message_event(callback)
	 */
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	/**
	 * only for message_event(callback)
	 */
	public Integer getPeer_id() {
		return peer_id;
	}
	public void setPeer_id(Integer peer_id) {
		this.peer_id = peer_id;
	}
	/**
	 * only for message_event(callback)
	 */
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
}
