package com.petya136900.rcebot.rce.timetable;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.pdfparser.PdfParser;
import com.petya136900.rcebot.pdfparser.TimetablePDF;
import com.petya136900.rcebot.rce.RCE;
import com.petya136900.rcebot.rce.timetable.TimetableException.ExceptionCode;
import com.petya136900.rcebot.tools.Foo;
import com.petya136900.rcebot.vk.VK;

@SuppressWarnings("unused")
public class TimetableServer {
	private static final HashMap<String,Semaphore> lockers = new HashMap<>();
	private static final HashMap<String,Boolean> days = new HashMap<>();
	
	private final String date;
	private final String groupName;
	public TimetableServer(String date, String groupName) {
		this.date=date;
		this.groupName=groupName;
	}
	public static Timetable getFromDB(String date, String groupName) throws TimetableException {
		Timetable tt;
		tt = MySqlConnector.getTimetableByDayAndGroup(date,groupName);
		if(tt==null) {
			tt = MySqlConnector.getTimetableByDayAndGroup(date,groupName.replace("-", "К-"));
		}
		if(tt==null) {
			tt = MySqlConnector.getTimetableByDayAndGroup(date,groupName.replace("К-", "-"));
		}
		if(tt!=null)
			tt.setFromDB(true);
		return tt;
	}

	private void checkDate(String date) throws TimetableException {
		TimetableDirectories.checkDirectories(date);
		FileType[] types = MySqlConnector.getFileTypes(date);
		String[] vers = RCE.checkForDate(date);	
		//System.out.println("БД: "+Arrays.toString(types));
		//System.out.println("RCE: "+Arrays.toString(vers));
		//System.out.println("БД: "+Arrays.toString(types));
		//System.out.println("RCE: "+Arrays.toString(vers));
		for (String ver : vers) {
			// Check file_type from Mysql
			// parse unparsed
			boolean isCalls = ver.contains("call");

			if(!isParsed(types,ver)) {
				//PDFParser.parse(date,ver);
				//System.out.println("Найдено непропарсенное расписание: ("+date+ver+".pdf)");
				try {
					PdfParser parser;
					if(!isCalls) {
						parser = new PdfParser(new URL(RCE.getRceHost()+"/"
							+RCE.getTTDir()+"/"+date+ver+".pdf"), VK.getTestMode());
					} else {
						parser = new PdfParser(new URL(RCE.getRceHost()+"/"
							+RCE.getTTDir()+"/%D0%A0%D0%B0%D1%81%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5%20%D0%B7%D0%B2%D0%BE%D0%BD%D0%BA%D0%BE%D0%B2%20%D0%BD%D0%B0%20"+date+(ver.replace("call", ""))+".pdf"), VK.getTestMode());	
					}
					MySqlConnector.prepareParse(date,ver,"2",false);
					TimetablePDF t = parser.parse();
					//System.out.println("Парс прошёл");
					//System.out.println(JsonParser.toJson(t));
					MySqlConnector.saveTimetable(t);
				} catch(IOException ioe) {
					ioe.printStackTrace();
					class MN {}
					throw new TimetableException(ExceptionCode.UNKWN_ERROR,Foo.getMethodName(MN.class),ioe.getLocalizedMessage(),ioe);
				} catch(Exception e) {
					e.printStackTrace();
					class MN {}
					throw new TimetableException(ExceptionCode.PARSING_EXCEPTION,Foo.getMethodName(MN.class),e.getLocalizedMessage(),e);
				}
			} else {
				//System.out.println("Already saved to DB: ("+date+ver+".pdf)");
			}
		}
		
	}
	private boolean isParsed(FileType[] types, String ver) {
		for(FileType type : types) {
			if(type.getVer().equals(ver)) {
				if(type.getParsed()) {
					return true;
				}
			}
		}
		return false;
	}
	private String threadName() {
		String tname = Thread.currentThread().getName();
		if(tname.length()==8) { // 1 digit
			return tname.replace("-", "-----");
		} else if(tname.length()==9) { // 2 digit
			return tname.replace("-", "---");
		} else { // 3+
			return tname.substring(0,10);
		}
	}
	private static Boolean getParsStatus(String day) {
		synchronized (days) {
			if(days.putIfAbsent(day, true)==null) {
				days.put(day, true);
				lockers.put(day, new Semaphore(1));
				return false;
			} else {
				if(days.get(day)) {
					return true;
				} else {
					days.put(day, true);
					lockers.put(day, new Semaphore(1));
					return false;
				}
			}
		}
	}
	public Timetable get(Boolean checkRce) throws TimetableException {
		try {
			if(checkRce) {
				Boolean parsStatus=getParsStatus(this.date);
				if(parsStatus) {
					//vk.reply(threadName()+": ожидает"+"("+this.date+")");
				}
				lockers.get(this.date).acquire();
				if(!parsStatus) {
					//vk.reply(threadName()+": будет парсить"+"("+this.date+")");
					try {
						checkDate(this.date);
					} catch(TimetableException te) {
						if(checkRce) {
							if(days.get(this.date)!=null) {
								days.remove(this.date);
							}
							lockers.get(this.date).release();
						}
						class MN {}
						throw new TimetableException(ExceptionCode.UNKWN_ERROR,Foo.getMethodName(MN.class),te.getLocalizedMessage(),te);
					}						
					//vk.reply(threadName()+": завершил парсинг"+"("+this.date+")");
				}
			}
			Timetable tt;
			try {
				tt = getFromDB(this.date,this.groupName);
			} catch(TimetableException te) {
				if(checkRce) {
					if(days.get(this.date)!=null) {
						days.remove(this.date);
					}
					lockers.get(this.date).release();
				}
				class MN {}
				throw new TimetableException(ExceptionCode.UNKWN_ERROR,Foo.getMethodName(MN.class),te.getLocalizedMessage(),te);
			}
			//vk.reply(threadName()+": получил расписание"+"("+this.date+")");
			//vk.reply(JsonParser.toJson(tt));
			if(checkRce) {
				if(days.get(this.date)!=null) {
					days.remove(this.date);
				}
				lockers.get(this.date).release();
			}			
			return tt;
		} catch (InterruptedException e) {
			class MN {}
			throw new TimetableException(ExceptionCode.UNKWN_ERROR,Foo.getMethodName(MN.class),e.getLocalizedMessage(),e);
		} 
	}
}
