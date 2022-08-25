package com.petya136900.rcebot.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.handlers.CabinetHandler;
import com.petya136900.rcebot.rce.timetable.CabResponse;
import com.petya136900.rcebot.rce.timetable.MainShelude;
import com.petya136900.rcebot.rce.timetable.TimetableClient;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.rce.timetable.TimetableResponse;
import com.petya136900.rcebot.rce.timetable.TimetableServer;
import com.petya136900.rcebot.rce.timetable.TimetableException.ExceptionCode;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.trash.Foo;

@RestController
public class API {
	@RequestMapping(value = "/api/pair", method = RequestMethod.GET)
	public String pairController(@RequestParam(value = "day", 
											   required = false, 
											   defaultValue = "none") String day, 
								 @RequestParam(value = "group", 
								 			   required = true, 
								 			   defaultValue = "none") String group) {
		//System.out.println("/API/pair");
		try {
			if(day.trim().equalsIgnoreCase("none")) {
				//System.out.println("/API/pair - текущий день");
				day=TimetableClient.getCurDay();
			} else {
				//System.out.println("/API/pair - день: "+day);
				day=TimetableClient.checkDate(day);
				if(day==null) {
					class MN {}; throw new TimetableException(ExceptionCode.BAD_DAY,Foo.getMethodName(MN.class),null,null);
				} 
			}
			if(group.equalsIgnoreCase("none")) {
				throw new TimetableException(ExceptionCode.SET_GROUP);
			}
			//System.out.println("/API/pair - день прошёл проверку");
			TimetableResponse tr = new TimetableClient().getTimetable(day,group,null,false,true);
			//System.out.println("/API/pair - получено расписание");
			tr.setError(false);
			return JsonParser.toJson(tr);
		} catch(TimetableException te) {
			return JsonParser.toJson(TimetableResponse.createError(te));
		}
	}
	@RequestMapping(value = "/api/cab", method = RequestMethod.GET)
	public String cabController(@RequestParam(value = "day", 
											   required = false, 
											   defaultValue = "none") String day, 
								 @RequestParam(value = "cab", 
								 			   required = true, 
								 			   defaultValue = "none") String cab) {
		try {
			if(day.trim().equalsIgnoreCase("none")) {
				day=TimetableClient.getCurDay();
			} else {
				day=TimetableClient.checkDate(day);
				if(day==null) {
					class MN {}; throw new TimetableException(ExceptionCode.BAD_DAY,Foo.getMethodName(MN.class),null,null);
				} 
			}
			if(cab.equalsIgnoreCase("none")) {
				throw new TimetableException(ExceptionCode.BAD_CAB);
			}
			cab=cab.trim().replaceAll("(\\$|'|;|\\\\|&|\"|\\*)", "");
			if(cab.length()>16) {
				cab = cab.substring(0,16);
			}
			if(cab.trim().length()<1) {
				throw new TimetableException(ExceptionCode.BAD_CAB);
			}			
			int dateInt = TimetableClient.getDayOfWeek(day);
			Boolean weekType = TimetableClient.getWeekType(day);
			new TimetableServer(day,"").get(true);
			MainShelude[] replaces = CabinetHandler.sortAscending(MySqlConnector.getAllTimetablesByCab(day,cab));
			String[] groupsWithReplace = MySqlConnector.getGroupsWithReplace(day);
			MainShelude[] mains = CabinetHandler.sortAscending(MySqlConnector.getMainsTimetables(TimetableClient.getDayOfWeekEng(dateInt),weekType,cab,groupsWithReplace));
			if((replaces.length<1)&(mains.length<1)) {
				throw new TimetableException(ExceptionCode.EMPTY_CAB);
			}
			return JsonParser.toJson(new CabResponse(replaces,mains));
		} catch(TimetableException te) {
			return JsonParser.toJson(TimetableResponse.createError(te));
		}
	}	
}
