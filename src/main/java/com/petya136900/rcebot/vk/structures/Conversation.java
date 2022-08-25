package com.petya136900.rcebot.vk.structures;

public class Conversation {
	private ConvPeer peer;
	private ChatSettings chat_settings;
	private long last_message_id;
	private long in_read;
	private long out_read;
	// sort_id;
	private long unread_count;
	private boolean is_marked_unread;
	private boolean important;
	private boolean unanswered;
	// can_write
	public ConvPeer getPeer() {
		return peer;
	}
	public void setPeer(ConvPeer peer) {
		this.peer = peer;
	}
	public long getLast_message_id() {
		return last_message_id;
	}
	public void setLast_message_id(long last_message_id) {
		this.last_message_id = last_message_id;
	}
	public long getIn_read() {
		return in_read;
	}
	public void setIn_read(long in_read) {
		this.in_read = in_read;
	}
	public long getOut_read() {
		return out_read;
	}
	public void setOut_read(long out_read) {
		this.out_read = out_read;
	}
	public long getUnread_count() {
		return unread_count;
	}
	public void setUnread_count(long unread_count) {
		this.unread_count = unread_count;
	}
	public boolean isIs_marked_unread() {
		return is_marked_unread;
	}
	public void setIs_marked_unread(boolean is_marked_unread) {
		this.is_marked_unread = is_marked_unread;
	}
	public boolean isImportant() {
		return important;
	}
	public void setImportant(boolean important) {
		this.important = important;
	}
	public boolean isUnanswered() {
		return unanswered;
	}
	public void setUnanswered(boolean unanswered) {
		this.unanswered = unanswered;
	}
	public ChatSettings getChat_settings() { return chat_settings; }
	public void setChat_settings(ChatSettings chat_settings) { this.chat_settings = chat_settings; }
}
