package com.petya136900.rcebot.rce.timetable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

import com.petya136900.rcebot.db.BotSettings;
import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.handlers.CreateKeyboardHandler;
import com.petya136900.rcebot.rce.timetable.TimetableException.ExceptionCode;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.trash.Foo;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.Payload;

public class TimetableClient {
	private String date;
	private String groupName;
	private Integer peerID;
	private static final String[] rusAlp = {"А","Б","В","Г","Д","Е","З","И","К","Л","М","Н","О","П","Р","С","Т","У","Ф","Х","Ц","а","б","в","г","д","е","з","и","к","л","м","н","о","п","р","с","т","у","ф","х","ц"};
	private static final String [] engAlp = {"A","B","V","G","D","E","Z","I","K","L","M","N","O","P","R","S","T","U","F","H","C","a","b","v","g","d","e","z","i","k","l","m","n","o","p","r","s","t","u","f","h","c"};	
	
	public static final String[] normalCalls = {
			 "8:00 - 8.55",
			 "9:00 - 9:45, 9:50 - 10:35",
			"10:50 - 11:35, 11:40 - 12:25",
			"12:45 - 13:30, 13:35 - 14:20",
			"14:30 - 15:15, 15:20 - 16:05",
			"16:15 - 17:00, 17:05 - 17:50",
			"-",
			"-"
	};
	public static final String[] saturdayCalls = {
			 "-",
			 "8:00 - 8:45, 8:50 - 9:35",
			 "9:45 - 10:30, 10:35 - 11:20",
			"11:30 - 12:15, 12:20 - 13:05",
			"13:15 - 14:00, 14:05 - 14:50",
			"15:00 - 16:00",
			"-",
			"-"
	};	
	
	public static final SimpleDateFormat FORMAT = new SimpleDateFormat("ddMMyyyy");
	public        TimetableResponse getTimetable(String date, String groupName, VK vk, Boolean callRequest, Boolean fromApi) throws TimetableException {
		return getTimetable(date, groupName, vk, callRequest, fromApi,false);
	}
	public        TimetableResponse getTimetable(String date, String groupName, VK vk, Boolean callRequest, Boolean fromApi, Boolean isAlex) throws TimetableException {
		if(!fromApi) {
			this.peerID=vk.getVK().getPeer_id();
		}
		String dbGroupName;
		if(fromApi) {
			dbGroupName="";
		} else {
			dbGroupName=MySqlConnector.getGroupNameByPeerID(peerID); // CHECK FROM DB
		}
		if(date==null) {
			this.date=getCurDay();
		} else {
			this.date=checkDate(date);
			if(this.date==null) {
				class MN {}; throw new TimetableException(ExceptionCode.BAD_DAY,Foo.getMethodName(MN.class),null,null);
			} 
		}
		if(isSunday(this.date)) {
			class MN {}; throw new TimetableException(ExceptionCode.SUNDAY,Foo.getMethodName(MN.class),null,null);
		}		
		if(groupName==null) {
			if(fromApi) {
				class MN {}; throw new TimetableException(ExceptionCode.SET_GROUP,Foo.getMethodName(MN.class),null,null);
			}
			this.groupName = dbGroupName; // CHECK FROM DB
		} else {
			if(groupName.trim().length()>0) {
				this.groupName=removeTrash(toRus(groupName));
			} else {
				this.groupName = dbGroupName;
			}
		}
		//sayWetData(vk);
		if(this.groupName==null) {
			class MN {}; throw new TimetableException(ExceptionCode.SET_GROUP,Foo.getMethodName(MN.class),null,null);
		} else {
			if(!(this.groupName.length()>0)) {
				class MN {}; throw new TimetableException(ExceptionCode.SET_GROUP,Foo.getMethodName(MN.class),null,null);
			} else if(!this.groupName.equals(dbGroupName)) {
				if(this.groupName.length()>16) {
					this.groupName=this.groupName.substring(0,16);
				}
				if(!fromApi) {
					MySqlConnector.setGroupNameByPeerID(peerID,this.groupName);
				}
			}
		}
		//System.out.println("/API/pair - получаю расписание..");
		Boolean rceDown = false;
		//
		Timetable tt=null;
		try {
			tt = new TimetableServer(this.date,this.groupName).get(true);
		} catch (TimetableException te) {
			Exception causedExc = te.getCausedException();
			if(causedExc instanceof TimetableException) {
				if(((TimetableException) causedExc).getCode().equals(TimetableException.ExceptionCode.RCE_UNAVAILABLE)) {
					System.err.println("RCE DOWN");
					rceDown=true;
				} else {
					throw te;	
				}
			} else {
				throw te;
			}
		}
		if(rceDown) {
			tt = MySqlConnector.getTimetableByDayAndGroup(this.date,this.groupName);
			if(tt==null) {
				tt = MySqlConnector.getTimetableByDayAndGroup(this.date,this.groupName.replace("-", "К-"));
			}
			if(tt==null) {
				tt = MySqlConnector.getTimetableByDayAndGroup(this.date,this.groupName.replace("К-", "-"));
			}	
		}
		//System.out.println("/API/pair - получил");
		Integer peerID=null;
		if(!fromApi) {
			peerID=vk.getVK().getPeer_id();
		}
		Boolean fromKeyboard=false;
		try {
			if(JsonParser.fromJson(vk.getVK().getPayload(),Payload.class).getData().equalsIgnoreCase("from_keyboard")) {
				fromKeyboard=true;
			}
		} catch(Exception e) {
			//
		}
		if(tt==null) {
			//System.out.println("/API/pair - замены нет.. получаю основное");
			//System.out.println("Цэ Основное расписание");
			//vk.reply("Замен для группы "+this.groupName.toUpperCase()+" не найдено");
			return sendMain(peerID,this.groupName,this.date,false,null,callRequest,fromApi,fromKeyboard,rceDown,isAlex);
			// MS
		} else {
			//System.out.println("/API/pair - получил замену");
			//System.out.println("Цэ замена");
			//System.out.println(tt.getPairs()[0].getPairName());
			return sendReplace(tt, peerID,this.groupName,this.date,false,null,callRequest,fromApi,fromKeyboard,rceDown,isAlex);
		}		
	}
	public static TimetableResponse sendMain(Integer peerID, String groupName,String date, Boolean notify, String lastPairs, Boolean callRequest, Boolean fromApi, Boolean fromKeyboard, Boolean rceDown) throws TimetableException {
		return sendMain(peerID, groupName, date, notify, lastPairs, callRequest, fromApi, fromKeyboard, rceDown,false);
	}
	public static TimetableResponse sendMain(Integer peerID, String groupName,String date, Boolean notify, String lastPairs, Boolean callRequest, Boolean fromApi, Boolean fromKeyboard, Boolean rceDown, Boolean isAlex) throws TimetableException {
		//DigestUtils.md5Hex(PASSWORD)
		StringBuilder responseTimetable = new StringBuilder("");
		if(isAlex) {
			responseTimetable.append("Пары Вам, Сэр!\n");
		}
		if(rceDown) {
			responseTimetable.append("[⚠Сайт Колледжа недоступен, результаты могут быть устаревшими!]\n\n");
		}
		if(notify) {
			responseTimetable.append("Уведомление!\n");
		}
		int dayOfWeek = getDayOfWeek(date);
		String dayOfWeekEng = getDayOfWeekEng(dayOfWeek);
		String dayOfWeekRus = getDayOfWeekRus(dayOfWeek);
		Boolean weekType = getWeekType(date);		
		String dayMessage = MySqlConnector.getDayMessage(date);
		String callReplaceMessage = MySqlConnector.getCallReplace(date);;
		//vk.reply("Получаю основное расписание..");
		BlockPair[] pairs = MySqlConnector.getMainShelude(groupName,dayOfWeekEng,weekType);
		BotSettings bs = MySqlConnector.getBotSettings();
		//
		Boolean isChat=null;
		Boolean keyboardReplaced=false;
		if(!fromApi) {
			isChat = peerID>=2000000000;
			keyboardReplaced = MySqlConnector.getKeyboardReplaced(peerID);
		} else {
			fromKeyboard=true;
		}
		//
		// (isChat?CreateKeyboardHandler.createDefaultChatKeyboard(peerID):null)
		//
		if(bs.getReplace_mainshelude()) {
			VK.sendMessage(peerID,bs.getTextForReplace(),null,(((!keyboardReplaced)&(!fromKeyboard))?(isChat?CreateKeyboardHandler.createDefaultChatKeyboard(peerID):CreateKeyboardHandler.DEFAULT_PM_KEYBOARD):null));
			return null;
		}
		if(pairs.length<1) {
			pairs = MySqlConnector.getMainShelude(groupName.replace("-", "к-"),dayOfWeekEng,weekType);
			if(pairs.length<1) {
				pairs = MySqlConnector.getMainShelude(groupName.replace("К-", "-"),dayOfWeekEng,weekType);
			}
			if(pairs.length<1) {
				if(fromApi) {
					throw new TimetableException(ExceptionCode.TIMETABLE_FOR_GROUP_NOT_FOUND);
				}
				if(!notify) {
					//if(bs.getReplace_mainshelude()) {
						//new VK().sendMessage(peerID,bs.getTextForReplace());
					//} else {
						VK.sendMessage(peerID,(rceDown?responseTimetable.toString():"")+"Для группы "+groupName+" не найдено расписания",null,(((!keyboardReplaced)&(!fromKeyboard))?(isChat?CreateKeyboardHandler.createDefaultChatKeyboard(peerID):CreateKeyboardHandler.DEFAULT_PM_KEYBOARD):null));
					//}
				}
				return null;
			}
		} else {

		}
		Timetable tt = new Timetable(pairs);
		TimetableResponse tr = new TimetableResponse(dayMessage,callReplaceMessage,tt);
		tr.setIsReplace(false);
		if(fromApi) {
			return tr;
		}
		responseTimetable.append(groupName+" | "+beautifyDate(date)+"\n"
				+"Основное расписание | "+dayOfWeekRus+" | "+(weekType?"Числитель":"Знаменатель")+"\n");
		for(BlockPair pair : pairs) {
			responseTimetable.append(pair.getPairNum()+" Пара: "+
			pair.getPairName()+((pair.getPairCab().trim().length()>0)?(" - "+pair.getPairCab().trim()):"")+"\n"+
					(((callReplaceMessage==null)&callRequest)?getPairCall(pair.getPairNum(),dayOfWeek):""));
		}
		if(callReplaceMessage!=null) {
			responseTimetable.append("Замена звонков\n"+callReplaceMessage+"\n");
		}
		if(dayMessage!=null) {
			responseTimetable.append("Сообщение\n"+dayMessage+"\n");
		}			
		String response = responseTimetable.toString();
		if(!fromApi) {
			if(!callRequest) {
				String md5String=DigestUtils.md5Hex(response.replaceFirst("Уведомление!", "").replaceFirst("Пары Вам, Сэр!", "").trim());
				if(md5String.equals(lastPairs)) {
					//System.out.println("ne");
					return tr;
				} else {
					if((addDay(getCurDay()).equals(date))|
							((dayOfWeek==2)&(notify))) {
						//System.out.println("ulp");
						MySqlConnector.updateLastPairs(peerID,md5String);
					} else {
						//System.out.println("nulp?");
					}
				}
			}
			VK.sendMessage(peerID,response,null,(((!keyboardReplaced)&(!fromKeyboard))?(isChat?CreateKeyboardHandler.createDefaultChatKeyboard(peerID):CreateKeyboardHandler.DEFAULT_PM_KEYBOARD):null));
		}
		return tr;
	}
	private static String getPairCall(Integer pairNum, int dayOfWeek) {
		return "Звонки: "+((dayOfWeek==7)?TimetableClient.saturdayCalls[pairNum]:TimetableClient.normalCalls[pairNum])+"\n";
	}
	public static TimetableResponse sendReplace(Timetable tt, Integer peerID, String groupName,String date, Boolean notify, String lastPairs, Boolean callRequest, Boolean fromApi, Boolean fromKeyboard, Boolean rceDown) throws TimetableException {
		return sendReplace(tt, peerID, groupName, date, notify, lastPairs, callRequest, fromApi, fromKeyboard, rceDown, false);
	}
	public static TimetableResponse sendReplace(Timetable tt, Integer peerID, String groupName,String date, Boolean notify, String lastPairs, Boolean callRequest, Boolean fromApi, Boolean fromKeyboard, Boolean rceDown, Boolean isAlex) throws TimetableException {
		if(fromApi) {
			System.out.println("/API/pair - sendReplace()");
		}
		StringBuilder responseTimetable = new StringBuilder("");
		if(isAlex) {
			responseTimetable.append("Пары Вам, Сэр!\n");
		}
		if(rceDown) {
			responseTimetable.append("[⚠Сайт Колледжа недоступен, результаты могут быть устаревшими]!\n\n");
		}
		if(notify) {
			responseTimetable.append("Уведомление!\n");
		}
		int dayOfWeek = getDayOfWeek(date);
		//String dayOfWeekEng = getDayOfWeekEng(dayOfWeek);
		String dayOfWeekRus = getDayOfWeekRus(dayOfWeek);
		Boolean weekType = getWeekType(date);		
		String dayMessage = MySqlConnector.getDayMessage(date);
		String callReplaceMessage = MySqlConnector.getCallReplace(date);
		TimetableResponse tr = new TimetableResponse(dayMessage,callReplaceMessage,tt);
		//
		Boolean isChat=null;
		Boolean keyboardReplaced=false;
		if(!fromApi) {
			isChat = peerID>=2000000000;
			keyboardReplaced = MySqlConnector.getKeyboardReplaced(peerID);
		} else {
			fromKeyboard=true;
		}
		// (((!keyboardReplaced)&(!fromKeyboard))?(isChat?CreateKeyboardHandler.createDefaultChatKeyboard(peerID):CreateKeyboardHandler.DEFAULT_PM_KEYBOARD):null)
		//
		tr.setIsReplace(true);
		if(fromApi) {
			return tr;
		}
		responseTimetable.append(groupName+" | "+beautifyDate(date)+"\n"
				+"Замена | "+dayOfWeekRus+" | "+(weekType?"Числитель":"Знаменатель")+"\n");
		if(tt.getPractic()) {
			responseTimetable.append("Практика\n");
			responseTimetable.append("Преподаватели: "+tt.getTeachers().replaceAll("Бурлаков Е.Д", "Митюков Р.С")+"\n");
		}
		for(BlockPair pair : tt.getPairs()) {
			responseTimetable.append(pair.getPairNum()+" Пара: "+
			pair.getPairName()+((pair.getPairCab().trim().length()>0)?(" - "+pair.getPairCab().trim()):"")+"\n"+
				(((callReplaceMessage==null)&callRequest)?getPairCall(pair.getPairNum(),dayOfWeek):""));
		}	
		if(callReplaceMessage!=null) {
			responseTimetable.append("Замена звонков\n"+callReplaceMessage+"\n");
		}
		if(dayMessage!=null) {
			responseTimetable.append("Сообщение\n"+dayMessage+"\n");
		}				
		String response = responseTimetable.toString();
		if(!fromApi) {
			if(!callRequest) {			
				String md5String=DigestUtils.md5Hex(response.replaceFirst("Уведомление!", "").replaceFirst("Пары Вам, Сэр!", "").trim());
				if(md5String.equals(lastPairs)) {
					//System.out.println("retulp");
					return tr;
				} else {
					if((addDay(getCurDay()).equals(date))|
							((dayOfWeek==2)&(notify))) {
						//System.out.println("ulp");
						MySqlConnector.updateLastPairs(peerID,md5String);
					} else {
						//System.out.println("nulp?");
					}
				}
			}
			VK.sendMessage(peerID,response,null,(((!keyboardReplaced)&(!fromKeyboard))?(isChat?CreateKeyboardHandler.createDefaultChatKeyboard(peerID):CreateKeyboardHandler.DEFAULT_PM_KEYBOARD):null));
		}
		return tr;
	}
	public static String beautifyDate(String date) {
		return date.substring(0, 2)+"."+date.substring(2,4)+"."+date.substring(4,8);
	}
	/**
	 * @return true - числитель, false - знаменатель
	 */	
	public static Boolean getWeekType(String date) throws TimetableException {
		Calendar c = Calendar.getInstance();
		BotSettings bs = MySqlConnector.getBotSettings();
		Date forFind;
		try {
			c.setTime(FORMAT.parse(date));
			if(bs.getWeekFix()) {
				forFind = FORMAT.parse("14012019");
			} else {
				forFind = FORMAT.parse("07012019");
			}
			Integer diff = (int) ((c.getTime().getTime()/(1000*60*60*24))-(forFind.getTime()/(1000*60*60*24)));
			if(((diff)/7)%2==0) {
				return true;
			} else {
				return false;			
			}			
		} catch(ParseException pe) {
			class MN {}; throw new TimetableException(ExceptionCode.UNKWN_ERROR,Foo.getMethodName(MN.class),pe.getLocalizedMessage(),pe);
		}
	}
	public static int getDayOfWeek(String date) throws TimetableException {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(FORMAT.parse(date));
		} catch (ParseException pe) {
			//pe.printStackTrace();
			class MN {}; throw new TimetableException(ExceptionCode.UNKWN_ERROR,Foo.getMethodName(MN.class),pe.getLocalizedMessage(),pe);
		}
		return c.get(Calendar.DAY_OF_WEEK);		
	}
	public static String getDayOfWeekRus(Integer weekDay) throws TimetableException {
		switch(weekDay) {
			case(1):
				return "Воскресенье";
			case(2):
				return "Понедельник";
			case(3):
				return "Вторник";
			case(4):
				return "Среда";
			case(5):
				return "Четверг";
			case(6):
				return "Пятница";
			case(7):
				return "Суббота";	
		}
		class MN {}; throw new TimetableException(ExceptionCode.UNKWN_ERROR,Foo.getMethodName(MN.class));
	}
	public static String getDayOfWeekEng(Integer weekDay) throws TimetableException {
		switch(weekDay) {
			case(1):
				return "sunday";
			case(2):
				return "monday";
			case(3):
				return "tuesday";
			case(4):
				return "wednesday";
			case(5):
				return "thursday";
			case(6):
				return "friday";
			case(7):
				return "saturday";	
		}
		class MN {}; throw new TimetableException(ExceptionCode.UNKWN_ERROR,Foo.getMethodName(MN.class));
	}
	@SuppressWarnings("unused")
	private void sayWetData(VK vk) {
		vk.reply("Group AfterParse: "+this.groupName
				+"\n"
					+"_Date AfterParse: "+this.date);
	}
	public static String removeTrash(String string) {
		string = RegexpTools.removeAllString(string, "(групп)+(.)?+(\\b|$)");
		string=string.replaceAll("\\s","").toUpperCase();
		StringBuilder sb = new StringBuilder("");
		String add="";
    	Boolean firstDash = false;
    	for(int i=0;i<string.length();i++) {
    		add=string.substring(i,i+1);
    		if((add).matches("[а-яА-Я0-9]")) {
    			sb.append(add);
    		}
    	}
    	String tempString = sb.toString();
    	sb.setLength(0);
    	for(int i=0;i<tempString.length();i++) {
    		add=tempString.substring(i,i+1);
    		if(!(add).matches("[0-9]")) {
    			sb.append(add);
    		} else {
    			if(!firstDash) {
    				sb.append("-"+add);
    				firstDash=true;
    			} else {
    				sb.append(add);
    			}
    		}    			
    	}
    	return sb.toString();
	}
	public static String toRus(String string) {
		StringBuilder sb = new StringBuilder("");
    	String add="";
        for (int i=0; i < string.length(); i++) {
        	add = string.substring(i,i+1);
        	for (int j=0; j < engAlp.length; j++) {
				if (engAlp[j].equals(add)) {
					add=rusAlp[j];
					break;
				}
        	}
        	sb.append(add);
        }
        return sb.toString();
	}
	private       boolean isSunday(String date) throws TimetableException {
    	try {
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(FORMAT.parse(date));
    		if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
    			return true;
    		}
    	} catch(ParseException pe) {
    		class MN {}; throw new TimetableException(ExceptionCode.BAD_DAY,Foo.getMethodName(MN.class),pe.getLocalizedMessage(),pe);
    	}		
		return false;
	}
	public static String getCurDay() {
        return new SimpleDateFormat("ddMMyyyy").format(new Date());
	}
    private static      String findDayWeek(int weekDayNumber) throws TimetableException {
    	String foundDate = getCurDay();
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
    	while(calendar.get(Calendar.DAY_OF_WEEK)!=weekDayNumber) {
    		foundDate=addDay(foundDate);
    		calendar.add(Calendar.DATE, 1);
    	}        
        return foundDate;
	}
	public static       String checkDate(String date) throws TimetableException {	
		date=date.trim();
		String dateNum=date.replaceAll("\\D", "");
		if(RegexpTools.checkRegexp("^(\\d{8})", dateNum)) {
			return dateNum.substring(0,8);
		} else {
			if(RegexpTools.checkRegexp("посл+.*?завтр", date)) {
				return addDay(addDay(getCurDay()));
			} else if(RegexpTools.checkRegexp("завтр", date)) {
				return addDay(getCurDay());
			} else if(RegexpTools.checkRegexp("сегодн", date)) {
				return getCurDay();
			} else if(RegexpTools.checkRegexp("понедельник", date)) {
				return findDayWeek(2);
			} else if(RegexpTools.checkRegexp("вторник", date)) {
				return findDayWeek(3);
			} else if(RegexpTools.checkRegexp("сред", date)) {
				return findDayWeek(4);
			} else if(RegexpTools.checkRegexp("четверг", date)) {
				return findDayWeek(5);
			} else if(RegexpTools.checkRegexp("пятниц", date)) {
				return findDayWeek(6);
			} else if(RegexpTools.checkRegexp("суббот", date)) {
				return findDayWeek(7);
			} else if(RegexpTools.checkRegexp("воскресень", date)) {
				class MN {}; throw new TimetableException(ExceptionCode.SUNDAY,Foo.getMethodName(MN.class),null,null);
			}
		}
		return null;
	}
	public static String addDay(String dateForConvert) throws TimetableException {
    	String date;
    	try {
    		date = FORMAT.format(new Date(FORMAT.parse(dateForConvert).getTime()+86400000));
    	} catch(ParseException pe) {
    		class MN {}; throw new TimetableException(ExceptionCode.BAD_DAY,Foo.getMethodName(MN.class),pe.getLocalizedMessage(),pe);
    	}
		return date;
    }  	
}
