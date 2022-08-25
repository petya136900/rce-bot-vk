package com.petya136900.rcebot.rce.timetable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.petya136900.rcebot.rce.timetable.TimetableException.ExceptionCode;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.Foo;
public class SQLData {
	private String day;
	private String ver;
	private String groupName;
	private Timetable jsonData;
	public SQLData(ResultSet rs) throws SQLException, TimetableException {
		this.day=sqlDateToDay(rs.getString("day"));
		this.ver=rs.getString("ver");
		this.groupName=rs.getString("groupName");
		this.jsonData=JsonParser.fromJson(rs.getString("jsonData"), Timetable.class);
	}
	public static String dayToSQLDate(String day) throws TimetableException {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").format(TimetableClient.FORMAT.parse(day));
    	} catch(ParseException pe) {
    		class MN {}; throw new TimetableException(ExceptionCode.BAD_DAY,Foo.getMethodName(MN.class),pe.getLocalizedMessage(),pe);
    	}		
	}
	public static String sqlDateToDay(String sqlDate) throws TimetableException {
		try {
			return TimetableClient.FORMAT.format(new SimpleDateFormat("yyyy-MM-dd").parse(sqlDate));
    	} catch(ParseException pe) {
    		class MN {}; throw new TimetableException(ExceptionCode.BAD_DAY,Foo.getMethodName(MN.class),pe.getLocalizedMessage(),pe);
    	}		
	}	
	public Timetable getJsonData() {
		return jsonData;
	}
	public void setJsonData(Timetable jsonData) {
		this.jsonData = jsonData;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
}