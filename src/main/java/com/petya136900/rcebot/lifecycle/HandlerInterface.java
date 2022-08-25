package com.petya136900.rcebot.lifecycle;

import com.petya136900.rcebot.vk.VK;

public interface HandlerInterface {
	void handle(VK vkContent);
	/**
	 * @return true, if callback-events can work with Stages in this handler </br> otherwise, previous stage ignored
	 */
	default boolean keyboardSupportStages() {
		return false;
	}
	default void callbackUnsupportedHandle(VK vkContent) {
		//
	}
}
