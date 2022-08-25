package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.ConversationItem;
import com.petya136900.rcebot.vk.structures.Filter;
import com.petya136900.rcebot.vk.structures.GetConversationsResponse;

public class RememberHandler implements HandlerInterface {
	private GetConversationsResponse response;
	@Override
	public void handle(VK vkContent) {
		String text = vkContent.getVK().getText();
		switch(vkContent.getVK().getStage()) {
			case("1"):
				if(VK.getAdminID().equals(vkContent.getVK().getFrom_id())) {
					response = VK.getConversations(Filter.unanswered);
					vkContent.reply("Не отвеченных бесед: "+response.getCount()
					+"\nРазослать уведомления? (да\\нет)");
					vkContent.getVK().setStage("quest");
				} else {
					vkContent.reply("Permission Denied");
				}
			break;
			case("quest"):
				Boolean answer = false;
				if(text.toLowerCase().contains("да")) {
					answer=true;
				}
				vkContent.getVK().removeStages();
				if(!answer) {
					vkContent.reply("Bye!");
					return;
				}
				for(ConversationItem item : response.getItems()) {
					VK.sendMessage(item.getConversation().getPeer().getId(),
							  "Привет, я снова онлайн!");
					try {
						Thread.sleep(100);
					} catch (InterruptedException ignored) {

					}
				}
			break;
		}
	}

}
