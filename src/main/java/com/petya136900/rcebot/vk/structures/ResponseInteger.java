package com.petya136900.rcebot.vk.structures;

public class ResponseInteger {
	private Integer response;
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
	public Integer getResponse() {
		return response;
	}
	public void setResponse(Integer response) {
		this.response = response;
	}
}
