package com.petya136900.rcebot.rce.teachers;

public class TeacherException extends Exception {
	private static final long serialVersionUID = 8198470048807306962L;
	private Type type;
	private Teacher teacher;
	public enum Type {
		ALREADY_ADDED,
		UNKNOWN_ERROR,
		BAD_PASSWORD,
		NOT_JSON,
		UNKNOWN_ACTION,
		EMPTY_NAME,
		BAD_ID,
		BUSY_BY,
		ALREADY_UNKNOWN, 
		BAD_PAIR_ID
	}
	public TeacherException(Type type) {
		this.type=type;
	}
	public Type getType() {
		return type;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
}
