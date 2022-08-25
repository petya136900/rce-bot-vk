package com.petya136900.rcebot.rce.timetable;

public class PairsData {
	private String password;
	private String ip;
	private String type;
	private String pairID;
	private String pairName;
	private String pairTeacherID;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPairID() {
		return pairID;
	}
	public void setPairID(String pairID) {
		this.pairID = pairID;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPairName() {
		return pairName;
	}
	public void setPairName(String pairName) {
		this.pairName = pairName;
	}
	public String getPairTeacherID() {
		return pairTeacherID;
	}
	public void setPairTeacherID(String pairTeacherID) {
		this.pairTeacherID = pairTeacherID;
	}
}
