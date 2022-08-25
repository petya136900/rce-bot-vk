package com.petya136900.rcebot.vk.structures;

import com.petya136900.rcebot.tools.JsonParser;

public class Response {
	String type;
	String description;
	public Response(ResponseType rt) throws Exception {
		switch(rt) {
			case OK:
				this.type="ok";
				this.description="";
			break;
			default:
				this.type=ResponseType.ERROR.toString();
				this.description=rt.toString();
			break;
		}
	}
	public String toJson() {
		return JsonParser.toJson(this);
	}
}
