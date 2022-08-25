package com.petya136900.rcebot.vk.structures;

import com.petya136900.rcebot.vk.structures.Button.Type;

public class ClientInfo {
	private String[] button_actions;
	private Boolean keyboard;
	private Boolean inline_keyboard;
	private Boolean carousel;
	private Integer lang_id;
	public Integer getLang_id() {
		return lang_id;
	}
	public void setLang_id(Integer lang_id) {
		this.lang_id = lang_id;
	}
	public Boolean getCarousel() {
		return carousel;
	}
	public void setCarousel(Boolean carousel) {
		this.carousel = carousel;
	}
	public Boolean getInline_keyboard() {
		return inline_keyboard;
	}
	public void setInline_keyboard(Boolean inline_keyboard) {
		this.inline_keyboard = inline_keyboard;
	}
	public Boolean getKeyboard() {
		return keyboard;
	}
	public void setKeyboard(Boolean keyboard) {
		this.keyboard = keyboard;
	}
	public String[] getButton_actions() {
		return button_actions;
	}
	public void setButton_actions(String[] button_actions) {
		this.button_actions = button_actions;
	}
	public boolean support(Type button_type) {
		String s_button_type = button_type.toString(); 
		if(button_actions==null) {
			return false;
		}
		for(String button_action : button_actions) {
			if(button_action.equals(s_button_type)) {
				return true;
			}
		}
		return false;
	}
}
