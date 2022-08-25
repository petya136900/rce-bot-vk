package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;

public class RegTestHandler implements HandlerInterface {
	private String login;
	private String password;
	
	@Override
	public void handle(VK vkContent) {
		String stage = vkContent.getVK().getStage();
		String text  = vkContent.getVK().getText().trim();
		switch(stage) {
			case("1"):
				vkContent.reply("Hello! Enter your login: ");
				vkContent.getVK().setStage("enter_login");
				break;
			case("enter_login"):
				if(text.length()<3) { 
					vkContent.reply("Login can't be shorter than 3 characters"); return; }
				login=text;
				vkContent.reply("Good boy! Now enter your password: ");
				vkContent.getVK().setStage("enter_pass");
				break;
			case("enter_pass"):
				if(text.length()<3) { 
					vkContent.reply("Password can't be shorter than 3 characters"); return; }
				password = text;
				vkContent.reply("Your Login: "+login+"\n"
						+ "Password: "+password);
				vkContent.getVK().removeStages();
				break;
		}
	}
}
