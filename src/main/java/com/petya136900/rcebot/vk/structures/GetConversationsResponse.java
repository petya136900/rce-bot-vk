package com.petya136900.rcebot.vk.structures;

public class GetConversationsResponse {
	private Integer count;
	private ConversationItem[] items;
	public ConversationItem[] getItems() {
		return items;
	}
	public void setItems(ConversationItem[] items) {
		this.items = items;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
}
