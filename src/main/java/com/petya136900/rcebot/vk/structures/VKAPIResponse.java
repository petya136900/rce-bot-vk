package com.petya136900.rcebot.vk.structures;

public class VKAPIResponse {
	private Response response;
	public class Response {
		private Integer count;
		private VKMessage[] items;
		public VKMessage[] getItems() {
			return items;
		}
		public void setItems(VKMessage[] items) {
			this.items = items;
		}
		public Integer getCount() {
			return count;
		}
		public void setCount(Integer count) {
			this.count = count;
		}
	}
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
}
