package com.petya136900.rcebot.vk.structures;

public class ConversationItem {
	private Conversation conversation;
	private VKMessage last_message;
	public VKMessage getLast_message() {
		return last_message;
	}
	public void setLast_message(VKMessage last_message) {
		this.last_message = last_message;
	}
	public Conversation getConversation() {
		return conversation;
	}
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}
}
