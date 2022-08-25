package com.petya136900.rcebot.web;

import com.petya136900.rcebot.rce.teachers.Teacher;
import com.petya136900.rcebot.rce.teachers.TeacherPair;
import com.petya136900.rcebot.tools.JsonParser;

public class WebResponse {
	private Boolean error;
	private String errorType;
	private String type;
	private String hash;
	private String desc;
	private Teacher[] teachers;
	private TeacherPair[] pairs;
	private Boolean found;
	private String groupName;
	public WebResponse() {
		
	}
	public Boolean getError() {
		return error;
	}
	public void setError(Boolean error) {
		this.error = error;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public static WebResponse createError(String errorType) {
		WebResponse response = new WebResponse();
		response.setError(true);
		response.setErrorType(errorType);
		return response;
	}
	public String toJson() {
		return JsonParser.toJson(this);
	}
	public String getDesc() {
		return desc;
	}
	public WebResponse setDesc(String desc) {
		this.desc = desc;
		return this;
	}
	public static WebResponse create(String type) {
		WebResponse response = new WebResponse();
		response.setError(false);
		response.setType(type);
		return response;
	}
	public Teacher[] getTeachers() {
		return teachers;
	}
	public void setTeachers(Teacher[] teachers) {
		this.teachers = teachers;
	}
	public TeacherPair[] getPairs() {
		return pairs;
	}
	public void setPairs(TeacherPair[] pairs) {
		this.pairs = pairs;
	}
	public static WebResponse createGroup(String groupName) {
		WebResponse response = new WebResponse();
		if(groupName==null) {
			response.setFound(false);
			return response;
		} else {
			response.setFound(true);
			response.setGroupName(groupName);
			return response;
		}
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Boolean getFound() {
		return found;
	}
	public void setFound(Boolean found) {
		this.found = found;
	}
}
