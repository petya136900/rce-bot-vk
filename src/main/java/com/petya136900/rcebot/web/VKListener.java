package com.petya136900.rcebot.web;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.petya136900.rcebot.lifecycle.Logger;
import com.petya136900.rcebot.lifecycle.MainHandler;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.other.CallBack;
import com.petya136900.rcebot.vk.structures.Response;
import com.petya136900.rcebot.vk.structures.ResponseType;

@RestController 
public class VKListener {		
	@RequestMapping(value = "/vk", method = RequestMethod.GET)
	public String vkControllerGet() throws Exception {
		return new Response(ResponseType.JUST_POST).toJson();
	}
	@RequestMapping(value = "/error")
	public String errorHtml() throws Exception {
		return new Response(ResponseType.NOT_FOUND).toJson();
	}
	@RequestMapping(value = "/vk", method = RequestMethod.POST)
	public String vkControllerPost(@RequestBody(required = false) String jsonRequest) throws Exception {
		//System.out.println(jsonRequest);
		if(jsonRequest==null) {
			return new Response(ResponseType.ERROR_EMPTY).toJson();
		}
		VK parsedMessage = new VK(jsonRequest).parse();
		switch(parsedMessage.getParseResult()) {
			case OK:
				// Handle Request in new Thread
				new Thread(new MainHandler(parsedMessage)).start();
				return "ok";
			case BAD_REQUEST:
				return new Response(ResponseType.BAD_REQUEST).toJson();
			case CONFIRM:
				Logger.printInfo("Подтверждение сервера..");
				return CallBack.getConfirmationCode();
			case NOT_JSON:
				return new Response(ResponseType.NOT_JSON).toJson();
			default:
				throw new Exception("Unknown case");		
		}
		//return new JsonParser().toJson(parsedMessage).replaceAll("( )+", " ");
	}
}
