package com.petya136900.rcebot.lifecycle;

import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.*;
import org.fusesource.jansi.AnsiConsole;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class Logger extends Thread {
	private static Boolean sendToVk=true;
	private static Boolean checkSql=true;
	private final LOGTYPE type;
	private String message;
	private VK vk;
	private static Integer logsPeerID=2000000052;
	public static Integer getLogsPeerID() {
		return logsPeerID;
	}
	public static void setLogsPeerID(Integer peerID) {
		logsPeerID=peerID;
	}
	public enum LOGTYPE {
		INFO,
		NEW_MESSAGE, 
		MESSAGE_EVENT
	}
	
	static {
		AnsiConsole.systemInstall();
	}
	
	public  Logger(LOGTYPE type, String message) {
		this.type=type;
		this.message=message;
	}
	public  Logger(LOGTYPE type, VK vk) {
		this.type=type;
		this.vk=vk;
	}
	synchronized private void localPrintInfo(String message) {
		System.out.println(ansi()
				.fgBrightBlue().a(getCurTime()).fg(DEFAULT)
				.a(" |")
				.fgBrightCyan().a(message).fg(DEFAULT)
				);		
		if(getSendToVk()) {
			VK.sendMessage(logsPeerID, getCurTime()+" |"+message);
		}
	}
	public  static void printInfo(String message) {
		new Thread(new Logger(LOGTYPE.INFO,message)).start();
	}	
	@Override
	public  void   run() {
		switch(this.type) {
		case INFO:
			localPrintInfo(this.message);
			break;
		case NEW_MESSAGE:
			System.out.println(ansi());
			localPrintNewMessage(vk.getVK().getMessage(),null);
			break;
		case MESSAGE_EVENT:
			localPrintNewEvent(vk);
			break;
		default:
			//
			break;
		}
	}
	private void localPrintNewEvent(VK vk) {
		Long curTime = System.currentTimeMillis();
		String groupName = null;
		Integer user_id = vk.getVK().getObject().getUser_id();
		Integer peer_id = vk.getVK().getObject().getPeer_id();
		Payload payload = vk.getVK().getObject().getPayload();
		String handler = payload.getHandler();
		String stage = payload.getStage();
		if(getCheckSql()) {
			try {
				groupName = MySqlConnector.getGroupNameByPeerID(peer_id);
			} catch(TimetableException te) {
				groupName=null;
			}
		}
		if(groupName==null) {
			groupName="$UNKWN";
		}
		Conversations conversations = VK.getConversationByid(peer_id);
		System.out.println(ansi()
				.fgBrightYellow().a(vk.isInternalMention()?convertDate(curTime):"")
				.fgBrightBlue().a(!vk.isInternalMention()?convertDate(curTime):"").fg(DEFAULT)
				.a(" |")
				.fgBrightYellow().a("Message Event")
				.a(" |")
				.fgBrightBlue().a("peer_id: ").fg(DEFAULT)
				.fg(CYAN).a(peer_id).fg(DEFAULT)
				.fg(YELLOW).a("("+groupName+")")
				.fg(CYAN).a(getConvTitle(conversations))
				.fg(DEFAULT)
				.a(" |")
				.fgBrightBlue().a("user_id: ").fg(DEFAULT)
				.fg(CYAN).a(user_id).fg(DEFAULT)
				.a(" |")
				.fgBrightBlue().a("message_id: ").fg(DEFAULT)
				.fg(CYAN).a(vk.getVK().getObject().getConversation_message_id()).fg(DEFAULT)
				.a(" |")
				.fgBrightBlue().a("handler: ").fg(DEFAULT)
				.fgBrightCyan().a(handler).fg(DEFAULT)
				.a(" |")
				.fgBrightBlue().a("stage: ").fg(DEFAULT)
				.fgBrightCyan().a(stage).fg(DEFAULT)
		);
		if(getSendToVk()) {
			VK.sendMessage(logsPeerID, convertDate(curTime)+"\n"
					+ "@CallBack-button\n"
					+ "peer_id: "+peer_id+"\n"
					+ "user_id: "+user_id+"\n"
					+ "handler: "+handler+"\n"
					+ "stage: "+stage);
		}
	}
	synchronized private void localPrintNewMessage(VKMessage message, StringBuilder sb) {
		Integer peer_id=message.getPeer_id();
		String groupName=null;
		//StringBuilder sb = new StringBuilder("");
		boolean rootQ=true;
		if(sb==null) {
			sb = new StringBuilder();
		} else {
			rootQ=false;
		}
		if(getCheckSql()) {
			try {
				groupName = MySqlConnector.getGroupNameByPeerID(peer_id);
			} catch(TimetableException te) {
				groupName=null;
			}
		}
		if(groupName==null) {
			groupName="$UNKWN";
		}
		Conversations conversations = VK.getConversationByid(vk.getVK().getPeer_id());
		User user = message.getUser();
		String senderName;
		if(message.getFrom_id()<0) {
			Group group = VK.getGroupById(message.getFrom_id());
			senderName = (group==null)?"[API ERROR]":group.getName();
		} else {
			senderName = (user==null)?"[API ERROR]":(user.getFirst_name()+" "+user.getLast_name());
		}
		System.out.println(ansi()
				.fgBrightYellow().a(vk.isInternalMention()?convertDate(message.getDate()):"")
				.fgBrightBlue().a(!vk.isInternalMention()?convertDate(message.getDate()):"").fg(DEFAULT)
				.a(" |")
				.fgBrightBlue().a("Peer: ").fg(DEFAULT)
				.fg(CYAN).a(message.getPeer_id()).fg(DEFAULT)
				.fg(YELLOW).a("("+groupName+")")
				.fg(CYAN).a(getConvTitle(conversations))
				.fg(DEFAULT)
				.a(" |")
				.fgBrightBlue().a("id: ").fg(DEFAULT)
				.fg(CYAN).a(message.getConversation_message_id()).fg(DEFAULT)
				.a(" |")
				.fgBrightBlue().a("From: ").fg(DEFAULT)
				.fg(YELLOW).a("("+message.getFrom_id()+") ").fg(DEFAULT)
				.fg(CYAN).a(senderName).fg(DEFAULT)
				.a(" |")
				//.fgBrightBlue().a("text: ").fg(DEFAULT)
				.fgBrightCyan().a(message.getText()).fg(DEFAULT)
			);
		//System.out.println("Message ID: "+message.getId());
		sb.append(
					convertDate(message.getDate())+"\n"+
					"peer_id: "+message.getPeer_id()+"("+groupName+")" +
					getConvTitle(conversations)+"\n"+
					"from_id: vk.com/"+(message.getFrom_id()>-1?"id":"club")+Math.abs(message.getFrom_id())+"\n"+
					"message_id: "+message.getConversation_message_id()+"\n"+
					"text: "+message.getText().replace("@all","@\\all")+"\n"
				);
		for(VKAttachment attachment : message.getAttachments()) {
			//String artist="";
			String transcript="";
			String title="";
			String desc="";
			String text="";			
			switch(attachment.getType()) {
				case("photo"):
					System.out.println(ansi()
							.fgBrightMagenta().a("\tКартинка:").fg(DEFAULT)
							.a(" |")
							.fgBrightYellow().a(
									((text = attachment.getPhoto().getText()).length()>0?("("+text+")"):"")).fg(DEFAULT)
							.a(" |")							
							.fgBrightBlue().a("URL: ")
							.fgBrightCyan().a(attachment.getPhoto().getMaxSize().getUrl()).fg(DEFAULT));
					sb.append(" Картинка: "+((text = attachment.getPhoto().getText()).length()>0?("("+text+")"):"")+"\n"
							+attachment.getPhoto().getMaxSize().getUrl()+"\n");
				break;
				case("video"):
					desc = ((desc = attachment.getVideo().getDescription())!=null?(desc.length()>0?(" - "+desc):""):"");
					System.out.println(ansi()
							.fgBrightMagenta().a("\tВидео:").fg(DEFAULT)
							.a(" |")
							.fgBrightYellow().a("("+attachment.getVideo().getTitle()+
									desc+")").fg(DEFAULT)
							.a(" |")
							.fgBrightCyan().a("URL: "+attachment.getVideo().getPlayer()).fg(DEFAULT));
					sb.append(" Видео: "+"("+attachment.getVideo().getTitle()+
							desc+")\n"+
							attachment.getVideo().getPlayer());
				break;
				case("doc"):
					System.out.println(ansi()
							.fgBrightMagenta().a("\tДокумент:").fg(DEFAULT)
							.a(" |")
							.fgBrightYellow().a(
									((title = attachment.getDoc().getTitle()).length()>0?("("+title+")"):"")).fg(DEFAULT)
							.a(" |")							
							.fgBrightBlue().a("URL: ")
							.fgBrightCyan().a(attachment.getDoc().getUrl()).fg(DEFAULT));
					sb.append(" Документ: "+((title = attachment.getDoc().getTitle()).length()>0?("("+title+")"):"")+"\n"
							+attachment.getDoc().getUrl());
				break;
				case("audio"):
					System.out.println(ansi()
							.fgBrightMagenta().a("\tАудио:").fg(DEFAULT)
							.a(" |")
							.fgBrightYellow().a(
									(attachment.getAudio().getArtist()+" - "+attachment.getAudio().getTitle())).fg(DEFAULT));		
					sb.append(" Аудио: "+attachment.getAudio().getArtist()+" - "+attachment.getAudio().getTitle()+"\n");
				break;
				case("graffiti"):
					System.out.println(ansi()
							.fgBrightMagenta().a("\tГраффити:").fg(DEFAULT)
							.a(" |")						
							.fgBrightBlue().a("URL: ")
							.fgBrightCyan().a(attachment.getGraffiti().getUrl()+"&access_key="+attachment.getGraffiti().getAccess_key()).fg(DEFAULT));
					sb.append(" Граффити: "+attachment.getGraffiti().getUrl()+"&access_key="+attachment.getGraffiti().getAccess_key()+"\n");
				break;		
				case("sticker"):
					System.out.println(ansi()
							.fgBrightMagenta().a("\tСтикер:").fg(DEFAULT)
							.a(" |")						
							.fgBrightBlue().a("URL: ")
							.fgBrightCyan().a(attachment.getSticker().getMaxImageWithBackground().getUrl()).fg(DEFAULT));		
					sb.append(" Стикер: "+attachment.getSticker().getMaxImageWithBackground().getUrl()+"\n");
				break;	
				case("audio_message"):
					System.out.println(ansi()
							.fgBrightMagenta().a("\tГолосовое сообщение:").fg(DEFAULT)
							.a(" |")						
							.fgBrightBlue().a("URL: ")
							.fgBrightCyan().a(attachment.getAudio_message().getLink_mp3()).fg(DEFAULT));
					sb.append(" Голосовое сообщение: \n"+attachment.getAudio_message().getLink_mp3()+"\n");
				if(attachment.getAudio_message().getDuration()<29) {
					if(attachment.getAudio_message().getTranscript_state()!=null) {
						System.out.print(ansi()
								.fgBrightMagenta().a("\tГолосовое сообщение:").fg(DEFAULT)
								.a(" |")						
								.fgBrightBlue().a("Текст: ")
								//.fgBrightCyan().a(vk.readTranscriptAudio(peer_id)).fg(DEFAULT));
								.fgBrightCyan().a(("("+attachment.getAudio_message().getTranscript_state()+")")));
						sb.append("Голосовое сообщение(Текст): "+("("+attachment.getAudio_message().getTranscript_state()+"): "));
						System.out.println(ansi().a(((transcript=attachment.getAudio_message().getTranscript()))!=null?transcript:"").fgDefault());
						sb.append(((transcript=attachment.getAudio_message().getTranscript()))!=null?transcript:"");
						sb.append("\n");
					}
				}				
				break;
			}
		}		
		if(message.getReply_message()!=null) {
			System.out.print(ansi().fg(YELLOW).a("\tОтвет на сообщение: ").fgDefault());
			sb.append("  Ответ на сообщение: \n");
			message.getReply_message().setPeer_id(peer_id);
			localPrintNewMessage(message.getReply_message(),sb);
		}
		if(message.getFwd_messages()!=null) {
			for(VKMessage fwd_message : message.getFwd_messages()) {
				System.out.print(ansi().fg(YELLOW).a("\tПересланное сообщение: ").fgDefault());
				sb.append("  Пересланное сообщение: \n");
				fwd_message.setPeer_id(peer_id);
				localPrintNewMessage(fwd_message,sb);
			}
		}
		if(rootQ) {
			if(getSendToVk()) {
				VK.sendMessage(logsPeerID, sb.toString());
			}
		}
	}

	private String getConvTitle(Conversations conversations) {
		if(conversations==null||conversations.isEmpty())
			return "";
		try {
			return " [" + (conversations.getItems()[0].getChat_settings().getTitle()) + "]";
		} catch (Exception e) {
			return "[failed]";
		}
	}

	private String convertDate(Long timeStamp) {
		return "["+new SimpleDateFormat("dd.MM HH:mm:ss").format(new Date((long)timeStamp*1000))+"]";
	}
	public  static void printNewMessage(VK vk) {
		new Thread(new Logger(LOGTYPE.NEW_MESSAGE,vk)).start();
	}
    public  static String getCurTime() {
        return "["+new SimpleDateFormat("dd.MM HH:mm:ss").format(new Date())+"]";
    }
	public static void printNewEvent(VK event) {
		new Thread(new Logger(LOGTYPE.MESSAGE_EVENT,event)).start();
	}
	public static Boolean getCheckSql() {
		return checkSql;
	}
	public static void setCheckSql(Boolean checkSql) {
		Logger.checkSql = checkSql;
	}
	public static Boolean getSendToVk() {
		return sendToVk;
	}
	public static void setSendToVk(Boolean sendToVk) {
		Logger.sendToVk = sendToVk;
	} 	
}
