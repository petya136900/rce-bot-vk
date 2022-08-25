package com.petya136900.rcebot.db;

import java.lang.reflect.Field;

import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.rce.timetable.TimetableException.ExceptionCode;
import com.petya136900.rcebot.tools.Foo;

public class BotSettings {
	private Boolean replace_mainshelude=false;
	private Boolean enabled=true;
	private String  textForReplace="@INFO | Замена не найдена\n@INFO | Заполнение основного расписания в процессе";
	private Boolean weekFix=false;
	private String domain="https://xn--j1al4b.xn--p1ai";
	private String path="/rasp/";
	
	public  Boolean getReplace_mainshelude() {
		return replace_mainshelude;
	}
	public  void    setReplace_mainshelude(Boolean replace_mainshelude) {
		this.replace_mainshelude = replace_mainshelude;
	}
	public  String  getTextForReplace() {
		return textForReplace;
	}
	public  void    setTextForReplace(String textForReplace) {
		this.textForReplace = textForReplace;
	}
	public  Boolean getEnabled() {
		return enabled;
	}
	public  void    setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public  String  getAll() throws TimetableException {
		StringBuilder sb = new StringBuilder("SETTINGS\n[");
		try {
			Class<? extends BotSettings> cl = this.getClass();
			Field[] df = cl.getDeclaredFields();
			Boolean first=true;
			for(Field part : df) {
				sb.append(String.format((!first?"\n\n":"")+part.getName()+"(%s) : %s", part.getType().getSimpleName(),part.get(this)));
				first=false;
			}
		} catch(Exception e) {
			e.printStackTrace();
			class MN {}; throw new TimetableException(ExceptionCode.UNKWN_ERROR_CODE,Foo.getMethodName(MN.class),e.getLocalizedMessage(),e,"1");
		}
		sb.append("]");
		return sb.toString();
	}
	public  String  get(String varName) throws TimetableException {
		try {
			Class<? extends BotSettings> cl = this.getClass();
			Field[] df = cl.getDeclaredFields();
			for(Field part : df) {
				if(part.getName().equalsIgnoreCase(varName)) {
					return String.format("Значение переменной %s(%s) - %s",part.getName(),part.getType().getSimpleName(),part.get(this));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			class MN {}; throw new TimetableException(ExceptionCode.UNKWN_ERROR_CODE,Foo.getMethodName(MN.class),e.getLocalizedMessage(),e,"2");
		}
		return "[ERROR] | Переменная "+varName+" не найдена";
	}
	public  String  set(String varName,String value) throws TimetableException {
		if(value.length()>4096) {
			value=value.substring(0,4096);
		}
		try {
			Class<? extends BotSettings> cl = this.getClass();
			Field[] df = cl.getDeclaredFields();
			Boolean comp=false;
			for(Field part : df) {
				if(part.getName().equalsIgnoreCase(varName)) {
					comp=true;
					if(part.get(this) instanceof String) {
						part.set(this, value);
					} else if(part.get(this) instanceof Integer) {
						try {
							part.set(this, Integer.parseInt(value));
						} catch(NumberFormatException nfe) {
							part.set(this, 0);
						}
					} else if(part.get(this) instanceof Boolean) {
						part.set(this, Boolean.parseBoolean(value));
					}
					break;					
				}
			}
			if(comp) {
				MySqlConnector.updateBotSettings(this);
				return "Значение "+value+" для переменной "+varName+" установлено";
			} else {
				return "Переменная "+varName+" не найдена";
			}
		} catch(TimetableException te) {
			throw te;
		} catch(Exception e) {
			e.printStackTrace();
			class MN {}; throw new TimetableException(ExceptionCode.UNKWN_ERROR_CODE,Foo.getMethodName(MN.class),e.getLocalizedMessage(),e,"3");
		}
	}
	public  void    setDefault() throws TimetableException {
		MySqlConnector.updateBotSettings(new BotSettings());
	}
	/**
	 * @return the weekFix
	 */
	public Boolean getWeekFix() {
		return weekFix;
	}
	/**
	 * @param weekFix the weekFix to set
	 */
	public void setWeekFix(Boolean weekFix) {
		this.weekFix = weekFix;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
}
