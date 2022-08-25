package com.petya136900.rcebot.rce.timetable;

public class CabsData {
	private Integer pairNum;
	private String group;
	private String pairName;
	public CabsData(Integer pairNum, String group, String pairName) {
		this.pairNum=pairNum;
		this.group=group;
		this.pairName=pairName;
	}
	public String getPairName() {
		return pairName;
	}
	public void setPairName(String pairName) {
		this.pairName = pairName;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public Integer getPairNum() {
		return pairNum;
	}
	public void setPairNum(Integer pairNum) {
		this.pairNum = pairNum;
	}
}
