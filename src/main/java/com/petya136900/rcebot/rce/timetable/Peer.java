package com.petya136900.rcebot.rce.timetable;

public class Peer {
	private Integer peer_id;
	private String groupName;
	private Boolean notifications;
	private String lastPairs;
	private Integer notifHour;
	private Integer workMode;
	public Integer getWorkMode() {
		return workMode;
	}
	public void setWorkMode(Integer workMode) {
		this.workMode = workMode;
	}
	public Integer getNotifHour() {
		return notifHour;
	}
	public void setNotifHour(Integer notifHour) {
		this.notifHour = notifHour;
	}
	public String getLastPairs() {
		return lastPairs;
	}
	public void setLastPairs(String lastPairs) {
		this.lastPairs = lastPairs;
	}
	public Boolean getNotifications() {
		return notifications;
	}
	public void setNotifications(Boolean notifications) {
		this.notifications = notifications;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Integer getPeer_id() {
		return peer_id;
	}
	public void setPeer_id(Integer peer_id) {
		this.peer_id = peer_id;
	}
}
