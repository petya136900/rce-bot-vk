package com.petya136900.rcebot.vk.structures;

import java.util.HashMap;

import com.petya136900.rcebot.lifecycle.DataStorage;
import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.lifecycle.MainHandler;
import com.petya136900.rcebot.lifecycle.Stage;
import com.petya136900.rcebot.vk.structures.VKMessage.Geo;

public class VKJson {
	private String type;
	private VKObject object;
	private Integer group_id;
	private String event_id;
	public VKJson(String type, VKObject object, Integer group_id, String event_id) {
		this.type=type;
		this.object=object;
		this.group_id=group_id;
		this.event_id=event_id;
	}
	public ClientInfo getClient_info() {
		return object.getClient_info();
	}
	public void setClient_info(ClientInfo client_info) {
		object.setClient_info(client_info);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public VKObject getObject() {
		return object;
	}
	public void setObject(VKObject object) {
		this.object = object;
	}
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}	
	public VKMessage getMessage() {
		return this.object.getMessage();
	}
	public void setMessage(VKMessage message) {
		this.object.setMessage(message);
	}	
	public Long getDate() {
		return this.object.getMessage().getDate();
	}
	public void setDate(Long date) {
		this.object.getMessage().setDate(date);
	}
	public String getStage() {
		if(this.object.getMessage().getStage()==null) {
			Stage st = new Stage();
			st.setName("1");
			st.setHandler(this.getHandler());
			//System.out.println("NS: "+st.getHandler().getClass().getCanonicalName());
			this.object.getMessage().setStage(st);
		}
		return this.object.getMessage().getStage().getName();
	}
	public void setStage(String stage) {
		
		Integer peer_id = this.getPeer_id();
		Integer from_id = this.getFrom_id();
		
		if(peer_id==null) {
			peer_id = getObject().getPeer_id();
		}
		if(from_id==null) {
			from_id = getObject().getUser_id();
		}
		
		String key = peer_id+"_"+from_id;
		stage = stage.trim();
		Stage st = new Stage();
		st.setName(stage);
		st.setHandler(this.getHandler());
		this.object.getMessage().setStage(st);
		if(stage.equals("1")) {
			MainHandler.getStages().remove(key);
		} else {
			MainHandler.getStages().put(key, this.object.getMessage().getStage());
		}
	}
	public Integer getFrom_id() {
		if(this.object.getMessage().getFrom_id()==null) {
			return getObject().getUser_id();
		}
		return this.object.getMessage().getFrom_id();
	}
	public void setFrom_id(Integer from_id) {
		this.object.getMessage().setFrom_id(from_id);
	}
	public Integer getId() {
		return this.object.getMessage().getId();
	}
	public void setId(Integer id) {
		this.object.getMessage().setId(id);
	}
	public Integer getPeer_id() {
		if(this.object.getMessage().getPeer_id()==null) {
			return getObject().getPeer_id();
		}
		return this.object.getMessage().getPeer_id();
	}
	public void setPeer_id(Integer peer_id) {
		this.object.getMessage().setPeer_id(peer_id);
	}
	public String getText() {
		return this.object.getMessage().getText();
	}
	public void setText(String text) {
		this.object.getMessage().setText(text);
	}
	public Integer getConversation_message_id() {
		return this.object.getMessage().getConversation_message_id();
	}
	public void setConversation_message_id(Integer conversation_message_id) {
		this.object.getMessage().setConversation_message_id(conversation_message_id);
	}
	public VKMessage[] getFwd_messages() {
		return this.object.getMessage().getFwd_messages();
	}
	public void setFwd_messages(VKMessage[] fwd_messages) {
		this.object.getMessage().setFwd_messages(fwd_messages);
	}
	public VKAttachment[] getAttachments() {
		return this.object.getMessage().getAttachments();
	}
	public void setAttachments(VKAttachment[] attachments) {
		this.object.getMessage().setAttachments(attachments);
	}
	public VKMessage getReply_message() {
		return this.object.getMessage().getReply_message();
	}
	public void setReply_message(VKMessage reply_message) {
		this.object.getMessage().setReply_message(reply_message);
	}
	public Geo getGeo() {
		return this.object.getMessage().getGeo();
	}	
	public HandlerInterface getHandler() {
		return this.object.getMessage().getHandler();
	}
	public void setHandler(HandlerInterface handler) {
		if(object.getMessage()==null) {
			object.setMessage(new VKMessage());
		}
		this.object.getMessage().setHandler(handler);
	}
	public void removeStages() {
		this.setStage("1");
	}
	public String getPayload() {
		return getObject().getMessage().getPayload();
	}
	public void setPayload(String payload) {
		getObject().getMessage().setPayload(payload);
	}
	public Payload getCallbackPayload() {
		return getObject().getPayload();
	}
	private Boolean isCallback = false;
	public void setIsCallback(boolean b) {
		isCallback = b;
	}
	public Boolean isCallback() {
		if(isCallback==null) {
			return false;
		}
		return isCallback;
	}
	private String getUnicKey() {
		return this.getPeer_id()+"_"+this.getFrom_id(); 
	}
	public VKJson dataSet(String key, Object value) {
		HashMap<String,Object> hm = DataStorage.getHM(getUnicKey());
		if(hm==null)
			DataStorage.putHM(getUnicKey()).put(key,value);
		return this;
	}
	public Object dataGet(String key) {
		HashMap<String,Object> hm = DataStorage.getHM(getUnicKey());
		if(hm==null)
			return null;
		return hm.get(key);
	}
	public VKJson dataRemove(String key) {
		HashMap<String,Object> hm = DataStorage.getHM(getUnicKey());
		if(hm!=null) 
			hm.remove(key);
		return this;
	}
	public VKJson dataClear() {
		DataStorage.clearHM(getUnicKey());
		return this;
	}
}
