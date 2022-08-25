package com.petya136900.rcebot.rce.timetable;

public class DateO {
	public DateO(String date) {
		this(date.substring(0, 2),date.substring(2,4),date.substring(4,8));
	}
	public DateO(String day, String month, String year) {
		if(day.length()!=2|month.length()!=2|year.length()!=4) 
			throw new IllegalArgumentException("Date must be dd.mm.YYYY format");
		this.day=day;
		this.month=month;
		this.year=year;
	}
	@Override
	public String toString() {
		return day+month+year;
	}
	private String day;
	private String month;
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	private String year;
}
