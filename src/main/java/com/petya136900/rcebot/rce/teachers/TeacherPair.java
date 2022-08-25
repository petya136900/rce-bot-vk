package com.petya136900.rcebot.rce.teachers;

import org.apache.commons.codec.digest.DigestUtils;

public class TeacherPair {
	private Integer id;
	private Integer type;
	private String name;
	private Integer teacherID;
	private String hash;
	public TeacherPair(String name) {
		this.name=name;
	}
	public TeacherPair(Integer id, Integer type, String name, Integer teacherID) {
		this.id=id;
		this.type=type;
		this.name=name;
		this.teacherID=teacherID;
	}
	/**
	 * @return the teacherID
	 */
	public Integer getTeacherID() {
		return teacherID;
	}
	/**
	 * @param teacherID the teacherID to set
	 */
	public void setTeacherID(Integer teacherID) {
		this.teacherID = teacherID;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
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
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public void updateHash() {
		StringBuilder sb=new StringBuilder(id+type+name+teacherID);
		this.hash=DigestUtils.md5Hex(sb.toString());
	}
}
