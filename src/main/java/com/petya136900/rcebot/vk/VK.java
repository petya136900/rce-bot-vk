package com.petya136900.rcebot.vk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.reflect.TypeToken;
import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.lifecycle.HandlerMapping;
import com.petya136900.rcebot.lifecycle.MainHandler;
import com.petya136900.rcebot.lifecycle.Mentions;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.MultipartUtility;
import com.petya136900.rcebot.tools.Settings;
import com.petya136900.rcebot.vk.other.CallBack;
import com.petya136900.rcebot.vk.other.LongPoll;
import com.petya136900.rcebot.vk.other.NullObject;
import com.petya136900.rcebot.vk.structures.*;
import com.petya136900.rcebot.vk.structures.Button.Type;
import com.petya136900.rcebot.vk.structures.MessageSendResponse.MessageInfo;
import com.petya136900.rcebot.vk.structures.User.name_case;
import com.petya136900.rcebot.vk.structures.VKAttachment.Audio_message;
import com.petya136900.rcebot.vk.structures.VKAttachment.Photo;

//import java.util.Arrays;

public class VK {
	private static String GROUP_TOKEN = "token"; //
	private static String API_VERSION = "5.104";
	private static Integer GROUP_ID;
	private static Integer ADMIN_ID;
	private static final Integer helperID=301082236;
	public final static Integer MAX_MESSAGE_LENGTH=3400;
	private static final Integer SPAM_MAX_RETRY_TIME=10;
	private static final Integer SPAM_DELAY_MS=300;
	private static final String VK_SCHEME = "https://";
	private static final String VK_METHOD_DOMAIN = "api.vk.com/method/";
	public void setInternalMention(boolean b) {
		if(vkJson!=null)
			if(vkJson.getMessage()!=null)
				vkJson.getMessage().setInternalIsMention(b);
	}
	private boolean isMention = false;
	private static Settings settings;
	public static void rerun() {
		if(settings==null)
			throw new IllegalArgumentException("The bot has not been run before");
		clearNames();
		setup(settings);
	}
	public static void setup(Settings settings) {
		VK.settings=settings;
		VK.setup(settings.getGroupToken(), // GROUP TOKEN (message permission)
				settings.getApiVersion());
		settings.addBotName("club"+GROUP_ID);
		if((settings.getNames()!=null)&&settings.getNames().length>0)
			VK.registerNames(settings.getNames());
		if(settings.getAdminID()!=null)
			VK.setAdminID(settings.getAdminID());
		if(settings.getTestMode()!=null)
			VK.setTestMode(settings.getTestMode(),true);
		if(settings.getConfirmCode()!=null)
			VK.setCallbackConfirmationCode(settings.getConfirmCode());
		if(!settings.getUseLongPoll()) {
			VK.startCallback(settings.getCallBackPort(),settings.getConsoleArgs()); // If use CallBack
		} else {
			VK.startLongPoll(settings.getApiVersion());
		}
	}

	public ClientInfo getClient_info() {
		return getVK().getClient_info();
	}
	public void setClient_info(ClientInfo client_info) {
		getVK().setClient_info(client_info);
	}
	
	public static Integer getAdminID() {
		return ADMIN_ID;
	}
	public static void setAdminID(Integer id) {
		VK.ADMIN_ID=id;
	}		
	public VK(String jsonRequest) {
		this.jsonRequest=jsonRequest;
	}
	public VK(VKJson update) {
		this.vkJson=update;
	}
	private static void setup(String groupToken,String apiVersion) {
		GROUP_TOKEN = groupToken;
		try {
			GROUP_ID = getCurrentGroup().getId();
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to obtain GroupID | Check GROUP_TOKEN in bot.ini");
		}
		System.out.println("GROUP_ID: "+GROUP_ID);
		API_VERSION=apiVersion;
	}

	public boolean isAdmin() {
		return MainHandler.checkAdmin(getVK().getFrom_id());
	}

	public boolean isInternalMention() {
		if(vkJson!=null)
			if(vkJson.getMessage()!=null)
				return vkJson.getMessage().getInternalIsMention();
		return false;
	}

	public enum ParseStatus {
		AWAITING,
		IN_PROGRESS,
		OK,
		CONFIRM,
		BAD_REQUEST,
		NOT_JSON
	}
	private ParseStatus parseStatus=ParseStatus.AWAITING;
	public ParseStatus getParseResult() {
		return parseStatus;
	}
	private String jsonRequest;
	private VKJson vkJson;
	public VKJson getVK() {
		return vkJson;
	}
	public VK parse() {
		parseStatus=ParseStatus.IN_PROGRESS;	
		if(!(JsonParser.isJson(jsonRequest))) {
			parseStatus=ParseStatus.NOT_JSON;
			return this;
		} else {
			//System.out.println("CB RAW: "+jsonRequest);
			try {
				vkJson=JsonParser.fromJson(jsonRequest, VKJson.class);
			} catch(Exception e) {
				vkJson=JsonParser.fromJson(jsonRequest, VKJsonCB.class).toVKJson();
			}
		}
		/*
		System.out.println("Event_id: "+vkJson.getEvent_id());
		System.out.println("Text: "+vkJson.getText());
		System.out.println("Type: "+vkJson.getType());
		System.out.println("Conversation_message_id: "+vkJson.getConversation_message_id());
		System.out.println("Date: "+vkJson.getDate());
		System.out.println("From_id: "+vkJson.getFrom_id());
		System.out.println("Group_id: "+vkJson.getGroup_id());
		System.out.println("Id: "+vkJson.getId());
		System.out.println("Peer_id: "+vkJson.getPeer_id());
		System.out.println("Attachments: "+Arrays.toString(vkJson.getAttachments()));
		System.out.println("Fwd_messages: "+Arrays.toString(vkJson.getFwd_messages()));
		//System.out.println("V: "+Arrays.toString(vkJson.getReply_message()));
		 */
		if(vkJson.getType()!=null) {
			if(vkJson.getType().equals("confirmation")) {
				if(vkJson.getGroup_id()!=null) {
					if(vkJson.getGroup_id().equals(VK.GROUP_ID)) {
						parseStatus=ParseStatus.CONFIRM;
					} else {
						parseStatus=ParseStatus.BAD_REQUEST;
					}
				} else {
					parseStatus=ParseStatus.BAD_REQUEST;
				}
			} else {
				parseStatus=ParseStatus.OK;
			}
		} else {
			parseStatus=ParseStatus.BAD_REQUEST;
		}
		return this;
	}
	public static String getVideos(Integer owner_id, Integer video_id,String access_key) {
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"video.get";
		String urlParams = "access_token="+VK.GROUP_TOKEN
		+"&v="+VK.API_VERSION
		+"&videos="+owner_id+"_"+video_id+"_"+access_key;
		String response = sendApiRequest(urlHost,urlParams);
		VKAPIResponse vkVideo;
		if(!(JsonParser.isJson(response))) {
			return null;
		} else {
			vkVideo=JsonParser.fromJson(response, VKAPIResponse.class);
		}
		String videoUrls="";
		Boolean first=true;
		for(VKMessage item: vkVideo.getResponse().getItems()) {
			if(!first) {
				videoUrls+="\n\t\t\t";
			}
			videoUrls+=item.getPlayer();
			first=false;
		}
		return videoUrls;
	}
	private static String sendApiRequest(String urlHost, String urlParams) {
		String response;
		try {
			 HttpURLConnection.setFollowRedirects(false);
		     HttpURLConnection conn = (HttpURLConnection) new URL(urlHost).openConnection();
		     conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		     conn.setRequestMethod("POST");
		     conn.setDoOutput(true);
		     conn.setConnectTimeout(26000);
		     BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
		     bw.write(urlParams);
		     bw.flush();
		     bw.close();
			 InputStream resultContentIS = conn.getInputStream();
	         BufferedReader reader = new BufferedReader(new InputStreamReader(resultContentIS, StandardCharsets.UTF_8));
	         response = reader.readLine();
	         //System.out.println(urlHost+"/?"+urlParams);
	         //System.out.println(response);
		} catch (IOException e) {
			response=null;
			System.err.println("Can't perform API request: "+urlHost+"?"+urlParams);
			e.printStackTrace();
		}
		return response;
	}
	public static VKMessage[] getByConversationMessageId(Integer peer_id, Integer conversation_message_id) {
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"messages.getByConversationMessageId";
		String urlParams = "access_token="+VK.GROUP_TOKEN
		+"&v="+VK.API_VERSION
		+"&peer_id="+peer_id
		+"&conversation_message_ids="+conversation_message_id;
		//System.out.println(urlHost+"?"+urlParams);
		String response = sendApiRequest(urlHost,urlParams);
		//System.out.println(response);
		return JsonParser.fromJson(response, VKAPIResponse.class).getResponse().getItems();
	}
	public static VKMessage getMessageById(Integer message_id) {
		VKMessage[] response = getMessagesByIds(message_id);
		if((response==null)||response.length<1) 
			return null;
		return response[0];
	}
	public static VKMessage[] getMessagesByIds(int ...message_ids) {
		String ids = Arrays.stream(message_ids)
				.filter(n->n>0)
				.mapToObj(String::valueOf)
				.collect(Collectors.joining(","));
		if(ids.length()<1) {
			return null;
		}
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"messages.getById";
		String urlParams = "access_token="+VK.GROUP_TOKEN
		+"&v="+VK.API_VERSION
		+"&message_ids="+ids;
		//System.out.println(urlHost+"?"+urlParams);
		String response = sendApiRequest(urlHost,urlParams);
		//System.out.println(response);
		return JsonParser.fromJson(response, VKAPIResponse.class).getResponse().getItems();
	}
	public static void registerNames(String[] names) {
		Mentions.registerNames(names);
	}
	public static void clearNames() {
		Mentions.clearNames();
	}
	public static boolean isReadByDefault() {
		return MainHandler.isReadByDefault();
	}
	public static void setReadByDefault(boolean readByDefault) {
		MainHandler.setReadByDefault(readByDefault);
	}
	public static void addHandler(String regexp, HandlerInterface handler) {
		HandlerMapping.addHandler(regexp,handler);
	}
	public static void setDefaultHandler(HandlerInterface defaultHandler) {
		HandlerMapping.setDefaultHandler(defaultHandler);
	}
	public static void setPerformOnlyHandler(boolean b) {
		HandlerMapping.setPerformOnlyHandler(b); 
	}
	public static void setTestMode(boolean testMode) {
		setTestMode(testMode,true);
	}
	public static void setTestMode(boolean testMode, boolean quite) {
		HandlerMapping.setTestMode(testMode);
		HandlerMapping.setQuiteTestMode(quite);
	}
	public static Integer getHelperID() {
		return helperID;
	}
	public static void registerBasicHandlers() {
		HandlerMapping.registerBasicHandlers();
	}
	public static void setCallbackConfirmationCode(String string) {
		CallBack.setConfirmationCode(string);
	}
	public static void startCallback(Integer port, String[] args) {
		CallBack.start(port,args);
	}
	public static void startLongPoll(String version) {
		LongPoll.start(version);
	}
	public static String getAPIVersion() {
		return API_VERSION;
	}
	public static LongPoll getLongPollServer(String apiVersion) {
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"groups.getLongPollServer";
		String urlParams = "access_token="+VK.GROUP_TOKEN
		+"&v="+apiVersion
		+"&group_id="+GROUP_ID;
		//System.out.println(urlHost+"?"+urlParams);
		String response = sendApiRequest(urlHost,urlParams);
		System.out.println(response);
		return JsonParser.fromJson(response, LongPoll.class);
	}
	public static LongPollObject getLongPollUpdates(String server, String key, String ts) {
		String urlHost = server;
		String urlParams = "act=a_check"
		+"&key="+key
		+"&ts="+ts
		+"&wait="+15;
		//System.out.println(urlHost+"?"+urlParams);
		String response = sendApiRequest(urlHost,urlParams);
		//System.out.println(response);
		//System.out.println("RAW:\n\n"+response);
		try {
			return JsonParser.fromJson(response, LongPollObject.class);
		} catch(Exception e) {
			return JsonParser.fromJson(response, LongPollObjectCB.class).toLongPollObject();
		}
	}
	public void replyImage(String imagePath) {
		Integer peer_id = this.getVK().getPeer_id();
		if(peer_id==null) {
			peer_id = getVK().getObject().getPeer_id();
		}
		VK.sendImage(peer_id, imagePath);
	}
	private class JsonServerPhotoHash {
		private Integer server;
		private String photo;
		private String hash;
		/**
		 * @return the hash
		 */
		public String getHash() {
			return hash;
		}
		/**
		 * @param hash the hash to set
		 */
		@SuppressWarnings("unused")
		public void setHash(String hash) {
			this.hash = hash;
		}
		/**
		 * @return the photo
		 */
		public String getPhoto() {
			return photo;
		}
		/**
		 * @param photo the photo to set
		 */
		@SuppressWarnings("unused")
		public void setPhoto(String photo) {
			this.photo = photo;
		}
		/**
		 * @return the server
		 */
		public Integer getServer() {
			return server;
		}
		/**
		 * @param server the server to set
		 */
		@SuppressWarnings("unused")
		public void setServer(Integer server) {
			this.server = server;
		}
	}
	public class CreatedImages {
		private VKAttachment.Photo[] response;

		/**
		 * @return the response
		 */
		public VKAttachment.Photo[] getResponse() {
			return response;
		}

		/**
		 * @param response the response to set
		 */
		public void setResponse(VKAttachment.Photo[] response) {
			this.response = response;
		}
	}
	public static void sendImage(Integer peer_id, String imagePath) {
		//MessagesUploadServer mus = getMessagesUploadServer(peer_id);
		MessagesUploadServer mus = getMessagesUploadServer();
		JsonServerPhotoHash jsph = uploadImage(mus.getResponse().getUpload_url(),imagePath);
		CreatedImages ci = saveMessagesPhoto(jsph);
		sendCreatedImages(peer_id,ci.getResponse());
	}
	public static Photo getUploadedPhoto(Integer peer_id, String imagePath) {
		//MessagesUploadServer mus = getMessagesUploadServer(peer_id);
		MessagesUploadServer mus = getMessagesUploadServer();
		JsonServerPhotoHash jsph = uploadImage(mus.getResponse().getUpload_url(),imagePath);
		return saveMessagesPhoto(jsph).getResponse()[0];
	}
	public static Photo getUploadedPhoto(Integer peer_id, URL url) {
		//System.out.println("GUP: "+peer_id+" | url: "+url);
		//MessagesUploadServer mus = getMessagesUploadServer(peer_id);
		MessagesUploadServer mus = getMessagesUploadServer();
		//System.out.println("MUS: "+JsonParser.toJson(mus));
		JsonServerPhotoHash jsph = uploadImage(mus.getResponse().getUpload_url(),url);
		//System.out.println("JSPH: "+JsonParser.toJson(jsph));
		return saveMessagesPhoto(jsph).getResponse()[0];
	}	
	public static void sendCreatedImages(Integer peer_id,Photo[] response) {
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"messages.send";
		String urlParams = "access_token="+VK.GROUP_TOKEN
				+"&v="+VK.API_VERSION
				+ "&peer_id="+peer_id
				+ "&random_id="+new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE-1);
		String attachments="&attachment=";
		Boolean first=true;
		int count=0;
		for(Photo image : response) {
			count++;
			if(count>10) {
				break;
			}
			if(!first) {
				attachments+=",";
			}
			first=false;
			attachments+="photo"+image.getOwner_id()+"_"+image.getId();
		}
		urlParams+=attachments;
		sendApiRequest(urlHost, urlParams);
	}
	private static CreatedImages saveMessagesPhoto(JsonServerPhotoHash jsph) {
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"photos.saveMessagesPhoto";
		String urlParams = "access_token="+VK.GROUP_TOKEN
		+"&photo="+jsph.getPhoto()
		+"&v="+VK.API_VERSION
		+"&server="+jsph.getServer()
		+"&hash="+jsph.getHash();
		String response = sendApiRequest(urlHost,urlParams);
		//System.out.println("CI: Request:\n "+urlHost+"?"+urlParams);
		//System.out.println("CI: Response:\n "+response);
		return JsonParser.fromJson(response, CreatedImages.class);
	}
	private static JsonServerPhotoHash uploadImage(String upload_url, String imagePath) {
		try {
			return sendMultipartToHost(upload_url,imagePath.toString());
		} catch(IOException ioe) {
			System.err.println("Не удалось загрузить изображение");
			ioe.printStackTrace();
			return null;
		}
	}
	private static JsonServerPhotoHash sendMultipartToHost(String upload_url, String imagePath) throws IOException {
		MultipartUtility multipart = new MultipartUtility(upload_url, "UTF-8");
		multipart.addFilePart("photo", new File(imagePath));
        return JsonParser.fromJson(multipart.finish().get(0), JsonServerPhotoHash.class);
	}
	//private static MessagesUploadServer getMessagesUploadServer(Integer peer_id) {
	private static MessagesUploadServer getMessagesUploadServer() {
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"photos.getMessagesUploadServer";
		String urlParams = "access_token="+VK.GROUP_TOKEN
		+"&v="+VK.API_VERSION
		//+"&peer_id="+peer_id;
		+"&group_id="+VK.GROUP_ID;
		String response = sendApiRequest(urlHost,urlParams);
		return JsonParser.fromJson(response, MessagesUploadServer.class);
	}
	public void replyImage(URL url) {
		Integer peer_id = this.getVK().getPeer_id();
		if(peer_id==null) {
			peer_id = getVK().getObject().getPeer_id();
		}
		VK.sendImage(peer_id, url);
	}
	public static void sendImage(Integer peer_id, URL url) {
		//MessagesUploadServer mus = getMessagesUploadServer(peer_id);
		MessagesUploadServer mus = getMessagesUploadServer();
		JsonServerPhotoHash jsph = uploadImage(mus.getResponse().getUpload_url(),url);
		CreatedImages ci = saveMessagesPhoto(jsph);
		sendCreatedImages(peer_id,ci.getResponse());
	}
	private static JsonServerPhotoHash uploadImage(String upload_url, URL url) {
		try {
			return sendMultipartToHost(upload_url,url);
		} catch(IOException ioe) {
			System.err.println("Не удалось загрузить изображение");
			ioe.printStackTrace();
			return null;
		}
	}
	private static JsonServerPhotoHash sendMultipartToHost(String upload_url, URL url) throws IOException {
		MultipartUtility multipart = new MultipartUtility(upload_url, "UTF-8");
		multipart.addFilePart("photo", url);
        return JsonParser.fromJson(multipart.finish().get(0), JsonServerPhotoHash.class);
	}
	public Photo getUploadedPhoto(URL url) {
		return getUploadedPhoto(this.getVK().getPeer_id(),url);
	}
	public Photo getUploadedPhoto(String imagePath) {
		return getUploadedPhoto(this.getVK().getPeer_id(),imagePath);
	}
	public void sendCreatedImages(Photo[] array) {
		sendCreatedImages(this.getVK().getPeer_id(),array);
	}
	public String getAudioMessageTranscript(int timeout) {
		Integer peer_id=this.getVK().getPeer_id();
		Integer conv_mess_id=this.getVK().getConversation_message_id();
		if(peer_id==null) {
			peer_id = getVK().getObject().getPeer_id();
		}
		if(conv_mess_id==null) {
			peer_id = getVK().getObject().getConversation_message_id();
		}
		return getAudioMessageTranscript(peer_id,conv_mess_id,10);
	}
	public static String getAudioMessageTranscript(Integer peer_id, Integer conversation_message_id, int timeout) {
		long startTime = System.currentTimeMillis();
		VKMessage message;
		Audio_message audioMessage = null;
		for(;(System.currentTimeMillis()-startTime)<(60*1000)
				&
			(System.currentTimeMillis()-startTime)<(timeout*1000);) {
			message = getByConversationMessageId(peer_id,conversation_message_id)[0];
			audioMessage = message.getAttachments()[0].getAudio_message();
			if(((audioMessage==null)||(audioMessage.getTranscript_state()==null))||
					(audioMessage.getTranscript_state().equalsIgnoreCase("in_progress"))) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException ignored) {}
				continue;
			} else if(audioMessage.getTranscript_state().equalsIgnoreCase("done")) {
				break;
			} else {
				return "";
			}
		}
		return audioMessage.getTranscript();		
	}
	public MessageInfo reply(String message) {
		return reply(message,null);
	}
	public MessageInfo reply(String message, String[] attachs) {	
		return reply(message,attachs,null);
	}
	public MessageInfo reply(String message, String[] attachs, Keyboard keyboard) {
		Integer peer_id = this.getVK().getPeer_id();
		if(peer_id==null) {
			peer_id = getVK().getObject().getPeer_id();
		}
		return sendMessage(peer_id, message,attachs,keyboard);	
	}
	public static MessageInfo sendMessage(Integer peer_id, String message) {
		return sendMessage(peer_id, message, null, null);
	}
	public static MessageInfo sendMessage(Integer peer_id, String message, String[] attachs) {
		return sendMessage(peer_id, message, attachs, null);
	}
	public static MessageInfo sendMessage(Integer peer_id, String message, String[] attachs, Keyboard keyboard) {
		String attachsString;
		String keyboardString;
		if(attachs!=null) {
			attachsString = String.join(",", attachs);
		} else {
			attachsString=null;
		}
		if(keyboard!=null) {
			keyboard.prepire();
			keyboardString = JsonParser.toJson(keyboard);
		} else {
			keyboardString=null;
		}
		Random rnd = new Random(System.currentTimeMillis());
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"messages.send";
		try {
			message = URLEncoder.encode(message, "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException ignored) {
		}
		Boolean success=false;
		Integer spamCount=0;
		String urlParams="";
		do {
			urlParams = "access_token="+VK.GROUP_TOKEN
			+"&v="+VK.API_VERSION
			+"&peer_ids="+peer_id
			+(attachsString!=null?("&attachment="+attachsString):(""))
			+(keyboardString!=null?("&keyboard="+keyboardString):(""))
			+"&random_id="+rnd.nextInt(Integer.MAX_VALUE-1)
			+"&message="+message;
			//System.out.println("Request:\n "+urlHost+"?"+urlParams);
			String response = sendApiRequest(urlHost,urlParams);
			//System.out.println(response);
			MessageSendResponse msResponse = JsonParser.fromJson(response, MessageSendResponse.class);
			if(msResponse.getError()==null) {
				success=true;
				return msResponse.getResponse()[0];
			} else {
				if(msResponse.getError().getError_code()!=null) {
					if(msResponse.getError().getError_code().equals(6)) {
						System.err.println("Flood-Control.. Delay..");
						try {Thread.sleep(SPAM_DELAY_MS);} catch (InterruptedException ignored) {};
						spamCount++;
					} else {
						System.err.println("Can't send message: \n"+response+"\n\nRequest: \n"+urlHost+"?"+urlParams);	
						return null;
					}
				}
			}
		} while((!success)&(spamCount<SPAM_MAX_RETRY_TIME));
		System.err.println("Can't send message: Flood control\n\nRequest: \n"+urlHost+"?"+urlParams);
		return null;
	}
	public boolean support(Type button_type) {
		return getVK().getClient_info().support(button_type);
	}
	public void sendMessageEventAnswer(MessageEventAnswer answer) {
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"messages.sendMessageEventAnswer";
		String urlParams = "access_token="+VK.GROUP_TOKEN
				+"&v="+VK.API_VERSION
				+"&peer_id="+answer.getPeer_id()
				+"&user_id="+answer.getUser_id()
				+"&event_id="+answer.getEvent_id()
				+"&event_data="+JsonParser.toJson(answer.getEvent_data());
		sendApiRequest(urlHost,urlParams);
	}
	public void replyMessageEventAnswer(EventData event_data) {
		MessageEventAnswer answer = new MessageEventAnswer(this.getVK().getObject().getPeer_id(),
														   this.getVK().getObject().getUser_id(),
														   this.getVK().getObject().getEvent_id(),
														   event_data);
		sendMessageEventAnswer(answer);		
	}
	public static GetConversationsResponse getConversations(Filter filter, Integer count) {
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"messages.getConversations";
		String urlParams = "access_token="+VK.GROUP_TOKEN
				+"&v="+VK.API_VERSION
				+"&group_id="+VK.GROUP_ID
				+"&filter="+filter
				+"&count="+count;
		String response = sendApiRequest(urlHost,urlParams);
		//System.out.println(response);
		GetConversationsResponseObject responseJson = JsonParser.fromJson(response,GetConversationsResponseObject.class);
		return responseJson.getResponse();
	}
	public static GetConversationsResponse getConversations(Filter filter) {
		return getConversations(filter,200);
	}
	public static boolean getTestMode() {
		return HandlerMapping.getTestMode();
	}
	public static void deleteMessages(int... message_ids) {
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"messages.delete";
		String ids = Arrays.stream(message_ids)
			.filter(n->n>0)
			.mapToObj(String::valueOf)
			.collect(Collectors.joining(","));
		if(ids.length()<1) {
			return;
		}
		Boolean success=false;
		Integer spamCount=0;
		do {
			String urlParams = "access_token="+VK.GROUP_TOKEN
					+"&v="+VK.API_VERSION
					+"&delete_for_all=1"
					+"&message_ids="+ids;
			String response = sendApiRequest(urlHost,urlParams);
			java.lang.reflect.Type type = new TypeToken<ResponseGen<NullObject>>(){}.getType();
			ResponseGen<NullObject> rg = JsonParser.fromJson(response, type);
			if(rg.getError()==null) {
				success=true;
				return;
			} else {
				if(rg.getError().getError_code()!=null) {
					if(rg.getError().getError_code().equals(6)) {
						System.err.println("Flood-Control.. Delay..");
						try {Thread.sleep(SPAM_DELAY_MS);} catch (InterruptedException ignored) {};
						spamCount++;
					} else {
						//System.err.println("Can't delete message: \n"+response+"\n\nRequest: \n"+urlHost+"?"+urlParams);	
						return;
					}
				}
			}
		} while((!success)&(spamCount<SPAM_MAX_RETRY_TIME));
	}
	// Just Message
	// By ID
	public static boolean editMessage(Integer messageId, String new_message) {
		return editMessage(messageId,new_message,null);
	}
	// By peerID + conv_id
	public static boolean editMessage(Integer peer_id, Integer conversation_message_id, String new_message) {
		return editMessage(peer_id,conversation_message_id,new_message,null);
	}
	// Message + Attachs[]
	// By ID
	public static boolean editMessage(Integer messageId, String new_message, String[] attachs) {
		return editMessage(messageId,new_message,attachs,null);
	}
	// By peerID + conv_id
	public static boolean editMessage(Integer peer_id, Integer conversation_message_id, String new_message, String[] attachs) {
		return editMessage(peer_id,conversation_message_id,new_message,attachs,null);
	}
	// Message + Attachs[] + Keyboard
	// By ID
	public static boolean editMessage(Integer messageId, String new_message, String[] attachs, Keyboard keyboard) {
		VKMessage message = VK.getMessageById(messageId);
		if(message==null) 
			return false;
		return editMessage(message.getPeer_id(), message.getConversation_message_id(), new_message, attachs, keyboard);
	}
	// By peerID + conv_id
	public static boolean editMessage(Integer peer_id, Integer conversation_message_id, String new_message, String[] attachs, Keyboard keyboard) {
		String attachsString;
		String keyboardString;
		if(attachs!=null) {
			attachsString = String.join(",", attachs);
		} else {
			attachsString=null;
		}
		if(keyboard!=null) {
			keyboard.prepire();
			keyboardString = JsonParser.toJson(keyboard);
		} else {
			keyboardString=null;
		}
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"messages.edit";
		try {
			new_message = URLEncoder.encode(new_message, "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException ignored) {
		}
		Boolean success=false;
		Integer spamCount=0;
		String urlParams="";
		do {
			urlParams = "access_token="+VK.GROUP_TOKEN
			+"&v="+VK.API_VERSION
			+"&peer_id="+peer_id
			+"&conversation_message_id="+conversation_message_id
			+(attachsString!=null?("&attachment="+attachsString):(""))
			+(keyboardString!=null?("&keyboard="+keyboardString):(""))
			+"&message="+new_message;
			//System.out.println("Request:\n "+urlHost+"?"+urlParams);
			String response = sendApiRequest(urlHost,urlParams);
			//System.out.println(response);
			ResponseInteger responseInt = JsonParser.fromJson(response, ResponseInteger.class);
			if(responseInt.getError()==null) {
				if(responseInt.getResponse().equals(1)) {
					success=true;
					return true;
				} return false;
			} else {
				if(responseInt.getError().getError_code()!=null) {
					if(responseInt.getError().getError_code().equals(6)) {
						System.err.println("Flood-Control.. Delay..");
						try {Thread.sleep(SPAM_DELAY_MS);} catch (InterruptedException ignored) {};
						spamCount++;
					} else {
						//System.err.println("Can't edit message: \n"+response+"\n\nRequest: \n"+urlHost+"?"+urlParams);	
						return false;
					}
				}
			}
		} while((!success)&(spamCount<SPAM_MAX_RETRY_TIME));
		System.err.println("Can't edit message: Flood control\n\nRequest: \n"+urlHost+"?"+urlParams);
		return false;
	}
	public static Long getServerTime() {
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"utils.getServerTime";
		String urlParams = "access_token="+VK.GROUP_TOKEN
				+"&v="+VK.API_VERSION;
		String response = sendApiRequest(urlHost,urlParams);
		java.lang.reflect.Type type = new TypeToken<ResponseGen<Long>>(){}.getType();
		ResponseGen<Long> fromJson = JsonParser.fromJson(response,type);
		return fromJson.getResponse();
	}
	public User getThisUser() {
		return getThisUser(new Field[]{});
	}
	public User getThisUser(Field ...fields) {
		return getThisUser(User.name_case.nom, fields);
	}
	public User getThisUser(name_case ncase, Field ...fields) {
		Integer fromID = getVK().getFrom_id();
		if(fromID==null)
			return null;
		return getUser(fromID+"",ncase,fields);
	}
	public static User getUser(String user_id, Field ...fields) {
		return getUser(user_id, User.name_case.nom, fields);
	}
	public static User getUser(String user_id, name_case ncase, Field ...fields) {
		User[] users = getUsers(new String[] {user_id}, ncase, fields);
		if(users!=null&&users.length>0)
			return users[0];
		return null;
	}
	public static User[] getUsers(String[] user_ids, Field ...fields) {
		return getUsers(user_ids, User.name_case.nom, fields);
	}
	public static User[] getUsers(String[] user_ids, name_case ncase, Field ...fields) {
		return getUsers(user_ids, User.name_case.nom, false, fields);
	}
	private static User[] getUsers(String[] user_ids, name_case ncase, Boolean forToken, Field ...fields) {
		String user_idsString = Stream.of(user_ids!=null?user_ids:new String[] {})
				.filter(x->x!=null&&x.length()>0)
				.collect(Collectors.joining(","));
		String fieldsString = Stream.of(fields)
				.filter(x->(x!=null)&&x!=Field.common_count)
				.map(x->x.toString())
				.collect(Collectors.joining(","));
		if((user_idsString==null||user_idsString.length()<1)&!forToken)
			return null;
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"users.get";
		String urlParams = "access_token="+VK.GROUP_TOKEN
				+"&v="+VK.API_VERSION
				+(!forToken?("&user_ids="+user_idsString):"")
				+(fieldsString.length()>0?("&fields="+fieldsString):"");
		String response = sendApiRequest(urlHost,urlParams);
		java.lang.reflect.Type type = new TypeToken<ResponseGen<User[]>>(){}.getType();
		ResponseGen<User[]> fromJson = JsonParser.fromJson(response,type);
		return fromJson.getResponse();
	}
	/*
	public static User getTokenUser() {
		return getTokenUser(new Field[]{});
	}
	public static User getTokenUser(Field ...fields) {
		return getTokenUser(User.name_case.nom, fields);
	}
	public User static getTokenUser(name_case ncase, Field ...fields) {
		User[] users = getUsers(null, User.name_case.nom, true, fields);
		if(users!=null&&users.length>0)
			return users[0];
		return null;
	}
	*/
	public static void markAsRead(Integer peer_id) {
		if(peer_id==null)
			return;
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"messages.markAsRead";
		String urlParams = "access_token="+VK.GROUP_TOKEN
				+"&v="+VK.API_VERSION
				+"&peer_id="+peer_id;
		sendApiRequest(urlHost,urlParams);
	}
	public static void markAsAnswered(Integer peer_id) {
		if(peer_id==null)
			return;
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"messages.markAsAnsweredConversation";
		String urlParams = "access_token="+VK.GROUP_TOKEN
				+"&v="+VK.API_VERSION
				+"&answered=1"
				+"&peer_id="+peer_id;
		sendApiRequest(urlHost,urlParams);
	}
	public static Group getGroupById(Integer groupID) {
		groupID = Math.abs(groupID);
		Group[] groups = getGroupsById(new Integer[]{groupID});
		if(groups==null||groups.length<1) 
			return null;
		return groups[0];
	}
	public static Group getCurrentGroup() {
		return getGroupsById("")[0];
	}
	public static Group[] getGroupsById(Integer... groupID) {
		String groupIDs = Stream.of(groupID)
				.distinct()
				.filter(x->x!=null)
				.map(x->(Math.abs(x)+""))
				.collect(Collectors.joining(","));
		if(groupIDs.length()<1) 
			return null;
		return getGroupsById(groupIDs);
	}
	public static Group[] getGroupsById(String ids) {
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"groups.getById";
		String urlParams = "access_token="+VK.GROUP_TOKEN
				+"&v="+VK.API_VERSION
				+"&fields=description"
				+"&group_ids="+ids;
		String response = sendApiRequest(urlHost,urlParams);
		java.lang.reflect.Type type = new TypeToken<ResponseGen<Group[]>>(){}.getType();
		ResponseGen<Group[]> fromJson = JsonParser.fromJson(response,type);
		return fromJson.getResponse();
	}
	public static void deleteMessageByPeerAndConv(Integer peer_id, Integer conversation_message_id) {
		deleteMessagesByPeerAndConv(peer_id,new Integer[] {conversation_message_id});
	}
	public static void deleteMessagesByPeerAndConv(Integer peer_id, Integer[] conversation_message_ids) {
		String ids = Stream.of(conversation_message_ids)
				.filter(x->x!=null)
				.map(x->x+"")
			    .collect(Collectors.joining(","));
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"messages.delete";
		String urlParams = "access_token="+VK.GROUP_TOKEN
				+"&v="+VK.API_VERSION
				+"&delete_for_all=1"
				+"&peer_id="+peer_id
				+"&conversation_message_ids="+ids
				+"&group_ids="+VK.GROUP_ID;
		sendApiRequest(urlHost,urlParams);
	}
	public static Conversations getConversationByid(Integer peerId) {
		if(peerId<2000000001)
			return Conversations.getEmpty();
		String urlHost = VK_SCHEME+VK_METHOD_DOMAIN+"messages.getConversationsById";
		String urlParams = "access_token="+VK.GROUP_TOKEN
				+"&v="+VK.API_VERSION
				+"&peer_ids="+peerId
				+"extended=true"
				+"group_id="+VK.GROUP_ID;
		String response = sendApiRequest(urlHost,urlParams);
		java.lang.reflect.Type type = new TypeToken<ResponseGen<Conversations>>(){}.getType();
		ResponseGen<Conversations> fromJson = JsonParser.fromJson(response,type);
		return fromJson.getResponse();
	}
}
