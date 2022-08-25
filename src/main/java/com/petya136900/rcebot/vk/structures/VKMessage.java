package com.petya136900.rcebot.vk.structures;

import java.lang.reflect.Field;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.lifecycle.Stage;
import com.petya136900.rcebot.vk.VK;

public class VKMessage {
	private String player;
	private Long date;
	private Integer from_id;
	private Integer id;
	private Integer peer_id;
	private Integer random_id;
	private Boolean important;
	private Integer out;
	private String text;
	private Integer conversation_message_id;
	private VKMessage[] fwd_messages;
	private VKAttachment[] attachments;
	private VKMessage reply_message;
	private Geo geo;
	private Stage stage;
	private String payload;
	private Boolean is_hidden;
	private HandlerInterface handler;
	public class Geo {
		private String type;
		private Coordinates coordinates;
		public class Coordinates {
			private float latitude;
			private float longitude;
			public Coordinates(float lon, float lat) {
				this.setLongitude(lon);
				this.setLatitude(lat);
			}
			public float getLongitude() {
				return longitude;
			}
			public void setLongitude(float longitude) {
				this.longitude = longitude;
			}
			public float getLatitude() {
				return latitude;
			}
			public void setLatitude(float latitude) {
				this.latitude = latitude;
			}
		}
		private Place place;
		public class Place {
			private String country;
			private String city;
			private String title;
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			public String getCity() {
				return city;
			}
			public void setCity(String city) {
				this.city = city;
			}
			public String getCountry() {
				return country;
			}
			public void setCountry(String country) {
				this.country = country;
			}                	 
		}
		public Place getPlace() {
			return place;
		}
		public void setPlace(Place place) {
			this.place = place;
		}
		public Coordinates getCoordinates() {
			return coordinates;
		}
		public void setCoordinates(Coordinates coordinates) {
			this.coordinates = coordinates;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public float[][][] get5points(float d) {
			float lon = this.getCoordinates().getLongitude();
			float lat = this.getCoordinates().getLatitude();
			return new float[][][] {{
					{lon-d,lat+d},
					{lon-d,lat-d},
					{lon+d,lat-d},
					{lon+d,lat+d},
					{lon-d,lat+d}
				}};
		}
	}
	private <T> String findValueName(Object foo, T fooVariable) { // foo - Class Container, fooVar - Class Variable
		int varHash = fooVariable.hashCode();
		Class<?> c = foo.getClass();
		String varName="UNKNOWN";
		for (Field field : c.getDeclaredFields()) {
			try {
				if(varHash==field.get(foo).hashCode()) {
					varName=field.getName();
					break;
				}
			} catch(IllegalAccessException e) {
				varName="IAE_ERROR";
			}
		}
		return varName;
	}		
	@Override
	public String toString() {
		return parseToString();
	}			
	private void checkV(Object value,StringBuilder sb,Boolean first) {
		if(value!=null) {
			if(value.toString().length()>0) {
				if(first) {
					first=false;
				} else {
					sb.append(",");
				}				
				sb.append("\""+findValueName(this,value)+"\":"+value);
			}
		}
	}			
	private String parseToString() {
		StringBuilder sb = new StringBuilder("{");
		Boolean first=true;
		checkV(date,sb,first);
		first=false;
		checkV(from_id,sb,first);
		checkV(text,sb,first);
		if(attachments.length>0) {
			checkV(attachments,sb,first);
		}
		checkV(conversation_message_id,sb,first);
		checkV(peer_id,sb,first);
		checkV(id,sb,first);
        sb.append("}");
		return sb.toString();
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public Integer getFrom_id() {
		return from_id;
	}
	public void setFrom_id(Integer from_id) {
		this.from_id = from_id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPeer_id() {
		return peer_id;
	}
	public void setPeer_id(Integer peer_id) {
		this.peer_id = peer_id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getConversation_message_id() {
		return conversation_message_id;
	}
	public void setConversation_message_id(Integer conversation_message_id) {
		this.conversation_message_id = conversation_message_id;
	}
	public VKMessage[] getFwd_messages() {
		return fwd_messages;
	}
	public void setFwd_messages(VKMessage[] fwd_messages) {
		this.fwd_messages = fwd_messages;
	}
	public VKAttachment[] getAttachments() {
		return attachments;
	}
	public void setAttachments(VKAttachment[] attachments) {
		this.attachments = attachments;
	}
	public VKMessage getReply_message() {
		return reply_message;
	}
	public void setReply_message(VKMessage reply_message) {
		this.reply_message = reply_message;
	}
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public Geo getGeo() {
		return geo;
	}
	public void setGeo(Geo geo) {
		this.geo = geo;
	}
	public Stage getStage() {
		return this.stage;
	}
	public void setStage(Stage stage) {
		this.stage=stage;
	}
	/**
	 * @return the handler
	 */
	public HandlerInterface getHandler() {
		return handler;
	}
	/**
	 * @param handler the handler to set
	 */
	public void setHandler(HandlerInterface handler) {
		this.handler = handler;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public Boolean getIs_hidden() {
		return is_hidden;
	}
	public void setIs_hidden(Boolean is_hidden) {
		this.is_hidden = is_hidden;
	}
	public Integer getRandom_id() {
		return random_id;
	}
	public void setRandom_id(Integer random_id) {
		this.random_id = random_id;
	}
	public Boolean getImportant() {
		return important;
	}
	public void setImportant(Boolean important) {
		this.important = important;
	}
	public Integer getOut() {
		return out;
	}
	public void setOut(Integer out) {
		this.out = out;
	}
	public User getUser(com.petya136900.rcebot.vk.structures.Field...fields) {
		Integer from_id = getFrom_id();
		if(from_id==null) 
			return null;
		return VK.getUser(""+from_id,fields);
	}
}