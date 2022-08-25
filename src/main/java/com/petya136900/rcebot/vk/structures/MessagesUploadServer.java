package com.petya136900.rcebot.vk.structures;

public class MessagesUploadServer {
	private Response response;
	public class Response {
		private String upload_url;
		private Integer album_id;
		private Integer user_id;
		/**
		 * @return the user_id
		 */
		public Integer getUser_id() {
			return user_id;
		}
		/**
		 * @param user_id the user_id to set
		 */
		public void setUser_id(Integer user_id) {
			this.user_id = user_id;
		}
		/**
		 * @return the album_id
		 */
		public Integer getAlbum_id() {
			return album_id;
		}
		/**
		 * @param album_id the album_id to set
		 */
		public void setAlbum_id(Integer album_id) {
			this.album_id = album_id;
		}
		/**
		 * @return the upload_url
		 */
		public String getUpload_url() {
			return upload_url;
		}
		/**
		 * @param upload_url the upload_url to set
		 */
		public void setUpload_url(String upload_url) {
			this.upload_url = upload_url;
		}		
	}
	/**
	 * @return the response
	 */
	public Response getResponse() {
		return response;
	}
	/**
	 * @param response the response to set
	 */
	public void setResponse(Response response) {
		this.response = response;
	}
}
