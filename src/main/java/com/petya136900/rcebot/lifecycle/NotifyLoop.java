package com.petya136900.rcebot.lifecycle;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.petya136900.rcebot.db.BotSettings;
import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.rce.timetable.PeerSettings;
import com.petya136900.rcebot.rce.timetable.Timetable;
import com.petya136900.rcebot.rce.timetable.TimetableClient;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.rce.timetable.TimetableServer;
import com.petya136900.rcebot.rce.timetable.TimetableException.ExceptionCode;
import com.petya136900.rcebot.tools.Foo;
import com.petya136900.rcebot.vk.VK;

public class NotifyLoop extends Thread {
	private static Thread thisThread;
	private static boolean worked = false;
	public NotifyLoop() {
		//
	}
	public static boolean isRunning() {
		if(thisThread==null)
			return false;
		return thisThread.isAlive();
	}
	public static void stopNotify(Boolean wait) {
		if(isRunning()) {
			worked = false;
			if (wait)
				if (thisThread != null)
					try {
						thisThread.interrupt();
						thisThread.join();
					} catch (Exception ignored) {}
		}
	}
	@Override
	public void run() {
		if(worked) {
			System.err.println("NotifyLoop already started");
			return;
		}
		thisThread=Thread.currentThread();
		worked=true;
		while(worked) {
			try {
				BotSettings bs = MySqlConnector.getBotSettings();
				if(bs.getEnabled()) {
					PeerSettings[] peers = MySqlConnector.getPeersWithNotify();
					Boolean first=true;
					Date currentDate = new Date();
					String formatedDay = getCurDay(currentDate);
					String tomorrowDay = addDay(formatedDay);
					Calendar cal = Calendar.getInstance();
					cal.setTime(currentDate);
					Integer dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
					if((dayOfWeek)==7) {
						tomorrowDay=addDay(tomorrowDay);
					}					
					Integer hour = cal.get(Calendar.HOUR_OF_DAY);					
					for(PeerSettings peer : peers) {
						if(hour>=peer.getNotifHour()) {
							//Timetable tt = new TimetableServer(tomorrowDay,peer.getGroupName()).get(first); // FirstTime - true
							Boolean rceDown=false;
							Timetable tt=null;
							try {
								tt = new TimetableServer(tomorrowDay,peer.getGroupName()).get(first);
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
								tt = MySqlConnector.getTimetableByDayAndGroup(tomorrowDay,peer.getGroupName());
								if(tt==null) {
									tt = MySqlConnector.getTimetableByDayAndGroup(tomorrowDay,peer.getGroupName().replace("-", "К-"));
								}
								if(tt==null) {
									tt = MySqlConnector.getTimetableByDayAndGroup(tomorrowDay,peer.getGroupName().replace("К-", "-"));
								}	
							}
							if(first) {
								first=false;
							}
							if(tt!=null) {
								//System.out.println("Отправляю замену:\n"+peer.getPeer_id()+"\n"+peer.getGroupName()+"\n"+tomorrowDay+"\n"+peer.getLastPairs());
								TimetableClient.sendReplace(tt, peer.getPeer_id(),peer.getGroupName(),tomorrowDay,true, peer.getLastPairs(),false,false,false,rceDown);
							} else {
								//System.out.println("Отправляю основу:\n"+peer.getPeer_id()+"\n"+peer.getGroupName()+"\n"+tomorrowDay+"\n"+peer.getLastPairs());
								if(peer.getWorkMode().equals(1)) {
									if(!bs.getReplace_mainshelude()) {
										TimetableClient.sendMain(peer.getPeer_id(),peer.getGroupName(),tomorrowDay,true, peer.getLastPairs(),false,false,false,rceDown);
									}
								}
							}
						}
						try {
							if(worked)
								Thread.sleep(50);
						} catch (InterruptedException ignored) {
							worked=false;
							break;
						}
					}
				}
				try {
					if(worked)
						Thread.sleep(1000*60*7);
				} catch (InterruptedException ignored) {
					worked=false;
				}
			} catch(TimetableException te) {
				if(!(te.getCode().equals(ExceptionCode.BAD_DAY))
						&!(te.getCode().equals(ExceptionCode.BAD_GROUP))
						&!(te.getCode().equals(ExceptionCode.SET_GROUP))
						&!(te.getCode().equals(ExceptionCode.SUNDAY))) {
							VK.sendMessage(VK.getAdminID(), te.messageErrorNotify());
						}				
			} catch (Exception ignored) {}
		}
		thisThread=null;
		worked=false; // if any exception happened
	}
	private String addDay(String day) throws TimetableException {
		try {
			return TimetableClient.FORMAT.format(new Date((TimetableClient.FORMAT.parse(day)).getTime()+86400000));
		} catch (ParseException pe) {
			class MN {}; throw new TimetableException(ExceptionCode.UNKWN_ERROR,Foo.getMethodName(MN.class),pe.getLocalizedMessage(),pe);
		}
	}
	private String getCurDay(Date currentDate) {
		return TimetableClient.FORMAT.format(currentDate);
	}
}
