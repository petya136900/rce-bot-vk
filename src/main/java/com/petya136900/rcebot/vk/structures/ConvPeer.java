package com.petya136900.rcebot.vk.structures;

public class ConvPeer {
	private Integer id;
	private String type;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getLocal_id() {
		return local_id;
	}
	public void setLocal_id(Integer local_id) {
		this.local_id = local_id;
	}
	private Integer local_id;
}
