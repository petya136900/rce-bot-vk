package com.petya136900.rcebot.rce.timetable;

public class TimetableResponse {
	private Boolean error;
	private String errorDesc;
	private String errorDescRus;
	private String dayMessage;
	private Boolean isReplace;
	private String callReplaceMessage;
	private Timetable timetable;
	public TimetableResponse(String dayMessage, String callReplaceMessage, Timetable timetable) {
		this.dayMessage=dayMessage;
		this.callReplaceMessage=callReplaceMessage;
		this.timetable=timetable;
	}
	public TimetableResponse() {
		// TODO Auto-generated constructor stub
	}
	public String getDayMessage() {
		return dayMessage;
	}
	public void setDayMessage(String dayMessage) {
		this.dayMessage = dayMessage;
	}
	public String getCallReplaceMessage() {
		return callReplaceMessage;
	}
	public void setCallReplaceMessage(String callReplaceMessage) {
		this.callReplaceMessage = callReplaceMessage;
	}
	public Timetable getTimetable() {
		return timetable;
	}
	public void setTimetable(Timetable timetable) {
		this.timetable = timetable;
	}
	public Boolean getError() {
		return error;
	}
	public void setError(Boolean error) {
		this.error = error;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public static TimetableResponse createError(TimetableException te) {
		TimetableResponse tr = new TimetableResponse();
		tr.setError(true);
		tr.setErrorDesc(te.getCode().toString());
		tr.setErrorDescRus(te.getMessage());
		return tr;
	}
	public Boolean getIsReplace() {
		return isReplace;
	}
	public void setIsReplace(Boolean isReplace) {
		this.isReplace = isReplace;
	}
	public String getErrorDescRus() {
		return errorDescRus;
	}
	public void setErrorDescRus(String errorDescRus) {
		this.errorDescRus = errorDescRus;
	}
}
