package com.petya136900.rcebot.vk.structures;

public class VKAPIVideoResponse {
	private Response response;
	public class Response {
		private Integer count;
		private Item[] items;
		public class Item {
			private String player;

			public String getPlayer() {
				return player;
			}

			public void setPlayer(String player) {
				this.player = player;
			}
		}
		public Item[] getItems() {
			return items;
		}
		public void setItems(Item[] items) {
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
