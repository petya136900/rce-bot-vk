package com.petya136900.rcebot.rce.teachers;

import org.apache.commons.codec.digest.DigestUtils;

public class Teacher {
	private Integer id;
	private String fullName;
	private String hash;
	public Teacher(int id, String fullName) {
		this.id=id;
		this.fullName=fullName;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public void updateHash() {
		this.hash=DigestUtils.md5Hex(id+fullName);
	}
}
