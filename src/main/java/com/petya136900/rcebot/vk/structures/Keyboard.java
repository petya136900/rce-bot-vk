package com.petya136900.rcebot.vk.structures;

import java.util.ArrayList;

public class Keyboard {
	private boolean one_time=true;
	transient private ArrayList<KeyboardLine> lines=new ArrayList<KeyboardLine>();
	private Button[][] buttons;
	private boolean inline=false;;
	/**
	 * if inline == false
	 * </br>
	 * 5 x 10 max size | 40 max keys
	 * </br>
	 * </br>
	 * if inline == true
	 * </br>
	 * 5 x 6 max size | 10 max keys
	 * 
	 */
	public Keyboard(KeyboardLine ...lines) {
		for(KeyboardLine line : lines) {
			if(line!=null) {
				this.lines.add(line);
			}
		}
	}
	public Keyboard addKeyboardLine(KeyboardLine line) {
		if(line!=null) {
			this.lines.add(line);
		}
		return this;
	}
	public Keyboard addKeyboardLines(KeyboardLine ...lines) {
		for(KeyboardLine line : lines) {
			if(line!=null) {
				this.lines.add(line);
			}
		}
		return this;
	}
	public Keyboard prepire() {
		this.buttons=new Button[lines.size()][];
		for(int i=0;i<lines.size();i++) {
			this.buttons[i]=lines.get(i).getButtons();
		}
		return this;
	}
	/**
	 * default true
	 */
	public boolean isOne_time() {
		return one_time;
	}
	/**
	 * default true
	 */
	public Keyboard setOne_time(boolean one_time) {
		this.one_time = one_time;
		return this;
	}
	/**
	 * default false
	 */
	public boolean isInline() {
		return inline;
	}
	/**
	 * default false
	 */
	public Keyboard setInline(boolean inline) {
		this.inline = inline;
		if(inline==true) {
			one_time=false;
		}
		return this;
	}

}
