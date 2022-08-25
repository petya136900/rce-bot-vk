package com.petya136900.rcebot.vk.structures;

public class VKJsonCB {
	private String type;
	private VKObjectCB object;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public VKObjectCB getObject() {
		return object;
	}
	public void setObject(VKObjectCB object) {
		this.object = object;
	}
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	private Integer group_id;
	private String event_id;
	public VKJson toVKJson() {
		VKObject object = this.object.toVKObject();
		return new VKJson(type,object,group_id,event_id);
	}
}
