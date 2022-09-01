package com.petya136900.rcebot.vk.other;

import com.petya136900.rcebot.lifecycle.MainHandler;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.LongPollObject;
import com.petya136900.rcebot.vk.structures.VKJson;

public class LongPoll extends Thread {
	private static Thread longPollThread;
	private Response response;
	private String oldTs=null;
	private Integer getUpdatesErrorCount=0;
	public class Response {
		private String key;
		private String server;
		private String ts;
		protected String getTs() {
			return ts;
		}
		protected void setTs(String ts) {
			this.ts = ts;
		}
		protected String getServer() {
			return server;
		}
		protected void setServer(String server) {
			this.server = server;
		}
		protected String getKey() {
			return key;
		}
		protected void setKey(String key) {
			this.key = key;
		}
	}
	
	private static String apiVersion=null;
	
	private static Boolean longPollEnabled=false;
	public static Boolean getEnabled() {
		return longPollEnabled;
	}
	synchronized public static void start(String version) {
		apiVersion=version;
		startLongPoll();
	}
	@Override 
	public void run() {
		LongPoll longPoll=VK.getLongPollServer(apiVersion); 
		System.out.println(longPoll.getInfo());
		while(!Thread.interrupted()) {
			VKJson[] updates = null;
			try {
				updates = longPoll.getUpdates();
				//System.out.println(JsonParser.toJson(updates));
				if(updates!=null) {
					for(VKJson update : updates) {
						// Handle Request in new Thread
						if(update!=null) {
							new Thread(new MainHandler(new VK(update))).start();
						}
					}
				}
				this.getUpdatesErrorCount=0;
			} catch(Exception e) {
				e.printStackTrace();
				System.err.println("Не удалось получить обновления");
				this.getUpdatesErrorCount++;
				updates=new VKJson[0];
				try {
					if(this.getUpdatesErrorCount<5) {
						Thread.sleep(150);
					} else {
						Thread.sleep(5000);	
					}
				} catch (InterruptedException ee) {
					throw new RuntimeException(ee);
				}
				longPoll=VK.getLongPollServer(apiVersion); 
				if((longPoll!=null)&&(this.getUpdatesErrorCount<4&&this.oldTs!=null)) {
					longPoll.setTs(oldTs);
				}
			}
		} 
		System.out.println("LongPoll остановлен..");
		longPollEnabled=false;
	}
	private VKJson[] getUpdates() throws Exception {
		this.oldTs = this.getTs();
		LongPollObject lpo = VK.getLongPollUpdates(this.getServer(),this.getKey(),this.getTs());
		//System.out.println(JsonParser.toJson(lpo.getUpdates()));
		if(lpo.getFailed()!=null) {
			System.out.println(JsonParser.toJson(lpo.getUpdates()));
			throw new Exception("LongPoll failed with code: "+lpo.getFailed());
		}
		this.oldTs=null;
		this.setTs(lpo.getTs());
		return lpo.getUpdates();
	}
	public static void stopLongPoll() {
		if(longPollThread!=null) {
			if(longPollEnabled) {
				longPollThread.interrupt();
			}
		}
	}
	private static void startLongPoll() {
		if(apiVersion==null) {
			apiVersion=VK.getAPIVersion();
		}
		if(!CallBack.getEnabled()) {
			if(!longPollEnabled) {
				longPollEnabled=true;
				System.out.println("Запуск LongPoll-сервера..");
				longPollThread = new LongPoll();
				longPollThread.setDaemon(false);
				longPollThread.start();
			} else {
				System.out.println("LongPoll уже запущен");
			}
		} else {
			System.out.println("Уже запущен CallBack-сервер");
		}		
	}
	private String getInfo() {
			return  "Key: "+this.getKey()+"\n"+
					"Server: "+this.getServer()+"\n"+
					"Ts: "+this.getTs();
	}
	public String getTs() {
		return response.getTs();
	}
	public void setTs(String ts) {
		response.setTs(ts);
	}
	public String getServer() {
		return response.getServer();
	}
	public void setServer(String server) {
		response.setServer(server);
	}
	public String getKey() {
		return response.getKey();
	}
	public void setKey(String key) {
		response.setKey(key);
	}
}
