package com.petya136900.rcebot.vk.structures;

import java.util.ArrayList;

public class KeyboardLine {
	private ArrayList<Button> buttons=new ArrayList<Button>();
	public KeyboardLine(Button ...buttons) {
		for(Button button : buttons) {
			if(button!=null) {
				this.buttons.add(button);
			}
		}
	}
	public KeyboardLine addButton(Button button) {
		if(button!=null) {
			this.buttons.add(button);
		}
		return this;
	}
	public KeyboardLine addButtons(Button ...buttons) {
		for(Button button : buttons) {
			if(button!=null) {
				this.buttons.add(button);
			}
		}
		return this;
	}
	public Button[] getButtons() {
		return buttons.toArray(new Button[buttons.size()]);
	}
}
