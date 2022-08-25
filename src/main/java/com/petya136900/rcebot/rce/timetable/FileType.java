package com.petya136900.rcebot.rce.timetable;

public class FileType {
	private String day;
	private String ver;
	private Integer type;
	private Boolean parsed;
	public FileType(String day, String ver, int type, int parsed) {
		this.day=day;
		this.ver=ver;
		this.type=type;
		if(parsed==1) {
			this.parsed=true;
		} else {
			this.parsed=false;
		}
	}
	@Override
	public String toString() {
		return ver+"-"+type+"-"+parsed;
	}
	public Boolean getParsed() {
		return parsed;
	}
	public void setParsed(Boolean parsed) {
		this.parsed = parsed;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
}
