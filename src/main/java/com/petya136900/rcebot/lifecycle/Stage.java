package com.petya136900.rcebot.lifecycle;

public class Stage {
	private String name="1"; // Default Stage
	private HandlerInterface handler;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the stage to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the hanlder
	 */
	public HandlerInterface getHandler() {
		return handler;
	}
	/**
	 * @param handler the handler to set
	 */
	public void setHandler(HandlerInterface handler) {
		this.handler = handler;
	}
}
