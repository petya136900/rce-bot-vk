package com.petya136900.rcebot.vk.structures;

import com.petya136900.rcebot.vk.VK;

public class MessageSendResponse {
	private MessageInfo[] response;
	public class MessageInfo {
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ((conversation_message_id == null) ? 0 : conversation_message_id.hashCode());
			result = prime * result + ((message_id == null) ? 0 : message_id.hashCode());
			result = prime * result + ((peer_id == null) ? 0 : peer_id.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			try {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				MessageInfo other = (MessageInfo) obj;
				if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
					return false;
				if (conversation_message_id == null) {
					if (other.conversation_message_id != null)
						return false;
				} else if (!conversation_message_id.equals(other.conversation_message_id))
					return false;
				if (message_id == null) {
					if (other.message_id != null)
						return false;
				} else if (!message_id.equals(other.message_id))
					return false;
				if (peer_id == null) {
					if (other.peer_id != null)
						return false;
				} else if (!peer_id.equals(other.peer_id))
					return false;
				return true;
			} catch (Exception e) {
				System.err.println("etq MSR: "+e);
				return false;
			}
		}
		private Integer peer_id;
		private Integer message_id;
		private Integer conversation_message_id;
		public void deleteMessage() {
			VK.deleteMessages(this.message_id);
		}
		public MessageInfo editMessageOrDeleteAndReply(String new_message, String[] attachs, Keyboard keyboard) {
			if(!editMessage(new_message,attachs,keyboard)) {
				deleteMessage();
				//System.out.println("Отправляю новое");
				return VK.sendMessage(peer_id, new_message,attachs,keyboard);
			} else {
				VK.markAsAnswered(peer_id);
			}
			return this;
		}
		public MessageInfo editMessageOrDeleteAndReply(String new_message, String[] attachs) {
			return editMessageOrDeleteAndReply(new_message,attachs,null);
		}
		public MessageInfo editMessageOrDeleteAndReply(String new_message) {
			return editMessageOrDeleteAndReply(new_message,null);
		}
		public boolean editMessage(String new_message) {
			return VK.editMessage(peer_id, conversation_message_id, new_message);
		}
		public boolean editMessage(String new_message, String[] attachs) {
			return VK.editMessage(peer_id, conversation_message_id, new_message, attachs);
		}
		public boolean editMessage(String new_message, String[] attachs, Keyboard keyboard) {
			return VK.editMessage(peer_id, conversation_message_id, new_message, attachs,keyboard);
		}
		public Integer getConversation_message_id() {
			return conversation_message_id;
		}
		public void setConversation_message_id(Integer conversation_message_id) {
			this.conversation_message_id = conversation_message_id;
		}
		public Integer getMessage_id() {
			return message_id;
		}
		public void setMessage_id(Integer message_id) {
			this.message_id = message_id;
		}
		public Integer getPeer_id() {
			return peer_id;
		}
		public void setPeer_id(Integer peer_id) {
			this.peer_id = peer_id;
		}
		private MessageSendResponse getEnclosingInstance() {
			return MessageSendResponse.this;
		}
	}
	private Error error;
	public class Error {
		private Integer error_code;
		private String error_msg;
		private RequestParam[] request_params;
		public class RequestParam {
			private String key;
			private String value;
			public String getKey() {
				return key;
			}
			public void setKey(String key) {
				this.key = key;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
		}
		public Integer getError_code() {
			return error_code;
		}
		public void setError_code(Integer error_code) {
			this.error_code = error_code;
		}
		public String getError_msg() {
			return error_msg;
		}
		public void setError_msg(String error_msg) {
			this.error_msg = error_msg;
		}
		public RequestParam[] getRequest_params() {
			return request_params;
		}
		public void setRequest_params(RequestParam[] request_params) {
			this.request_params = request_params;
		}
	}
	public Error getError() {
		return error;
	}
	public void setError(Error error) {
		this.error = error;
	}
	public MessageInfo[] getResponse() {
		return response;
	}
	public void setResponse(MessageInfo[] response) {
		this.response = response;
	}
}
