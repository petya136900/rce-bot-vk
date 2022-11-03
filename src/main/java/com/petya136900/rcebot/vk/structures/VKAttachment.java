package com.petya136900.rcebot.vk.structures;
public class VKAttachment {
	private String type;
	private Photo photo;
	private Audio audio;
	private Audio_message audio_message;
	private Sticker sticker;
	private Doc doc;
	private Graffiti graffiti;
	private Video video;
	private Link link;
	private Wall wall;
	public class Wall {
		private Integer id;
		private Integer owner_id;
		/**
		 * Deprecated (versions below 5.7)
		 */
		private Integer to_id;
		/**
		 * ID of the author
		 */
		private Integer from_id;
		private String access_key;
		private Long date;
		private String text;
		private Integer created_by;
		private Integer reply_owner_id;
		private Integer reply_post_id;
		private Integer friends_only;
		private Comments comments;
		private Copyright copyright;
		private Views views;
		private Donut donut;
		private Likes likes;
		private Reposts reposts;
		/**
		 * post, copy, reply, postpone, suggest
		 */
		private String post_type;
		private PostSource post_source;
		private VKAttachment[] attachments;
		private VKMessage.Geo geo;
		private Integer signer_id;
		private Wall[] copy_history;
		private Integer can_pin;
		private Integer can_delete;
		private Integer can_edit;
		private Integer is_pinned;
		private Integer marked_as_ads;
		private Boolean is_favorite;
		private Integer postponed_id;
		private double short_text_rate;
		private Boolean can_archive;
		private Boolean is_archived;
		private Boolean zoom_text;
		private From from;
		public class From {
			private Integer id;
			/**
			 * group, page, event
			 *
			 * profile, ..?
			 */
			private String name; // for communities
			private String first_name;
			private String last_name;
			private String screen_name;
			private String type;
			private String photo_50;
			private String photo_100;
			private Boolean can_access_closed;
			public Integer getId() {
				return id;
			}
			public void setId(Integer id) {
				this.id = id;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getFirst_name() {
				return first_name;
			}
			public void setFirst_name(String first_name) {
				this.first_name = first_name;
			}
			public String getLast_name() {
				return last_name;
			}
			public void setLast_name(String last_name) {
				this.last_name = last_name;
			}
			public String getScreen_name() {
				return screen_name;
			}
			public void setScreen_name(String screen_name) {
				this.screen_name = screen_name;
			}
			public String getType() {
				return type;
			}
			public void setType(String type) {
				this.type = type;
			}
			public String getPhoto_50() {
				return photo_50;
			}
			public void setPhoto_50(String photo_50) {
				this.photo_50 = photo_50;
			}
			public String getPhoto_100() {
				return photo_100;
			}
			public void setPhoto_100(String photo_100) {
				this.photo_100 = photo_100;
			}
			public Boolean getCan_access_closed() {
				return can_access_closed;
			}
			public void setCan_access_closed(Boolean can_access_closed) {
				this.can_access_closed = can_access_closed;
			}
		}
		public class Donut {
			private Boolean is_donut;
			private Integer paid_duration;
			private Boolean can_publish_free_copy;
			// private Object placeholder;
			/**
			 * all, duration
			 */
			private String edit_mode;
			public Boolean getIs_donut() {
				return is_donut;
			}
			public void setIs_donut(Boolean is_donut) {
				this.is_donut = is_donut;
			}
			public Integer getPaid_duration() {
				return paid_duration;
			}
			public void setPaid_duration(Integer paid_duration) {
				this.paid_duration = paid_duration;
			}
			public Boolean getCan_publish_free_copy() {
				return can_publish_free_copy;
			}
			public void setCan_publish_free_copy(Boolean can_publish_free_copy) {
				this.can_publish_free_copy = can_publish_free_copy;
			}
			public String getEdit_mode() {
				return edit_mode;
			}
			public void setEdit_mode(String edit_mode) {
				this.edit_mode = edit_mode;
			}
		}
		public class Views {
			private Integer count;
			public Integer getCount() {
				return count;
			}
			public void setCount(Integer count) {
				this.count = count;
			}
		}
		public class Copyright {
			private Integer id;
			private String link;
			private String name;
			private String type;
			public Integer getId() {
				return id;
			}
			public void setId(Integer id) {
				this.id = id;
			}
			public String getLink() {
				return link;
			}
			public void setLink(String link) {
				this.link = link;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getType() {
				return type;
			}
			public void setType(String type) {
				this.type = type;
			}
		}
		public String getAccess_key() {
			return access_key;
		}
		public void setAccess_key(String access_key) {
			this.access_key = access_key;
		}
		public Views getViews() {
			return views;
		}
		public void setViews(Views views) {
			this.views = views;
		}
		public Donut getDonut() {
			return donut;
		}
		public void setDonut(Donut donut) {
			this.donut = donut;
		}
		public Integer getPostponed_id() {
			return postponed_id;
		}
		public void setPostponed_id(Integer postponed_id) {
			this.postponed_id = postponed_id;
		}
		public double getShort_text_rate() {
			return short_text_rate;
		}
		public void setShort_text_rate(double short_text_rate) {
			this.short_text_rate = short_text_rate;
		}
		public Boolean getCan_archive() {
			return can_archive;
		}
		public void setCan_archive(Boolean can_archive) {
			this.can_archive = can_archive;
		}
		public Boolean getIs_archived() {
			return is_archived;
		}
		public void setIs_archived(Boolean is_archived) {
			this.is_archived = is_archived;
		}
		public Boolean getZoom_text() {
			return zoom_text;
		}
		public void setZoom_text(Boolean zoom_text) {
			this.zoom_text = zoom_text;
		}
		public From getFrom() {
			return from;
		}
		public void setFrom(From from) {
			this.from = from;
		}
		public Integer getFriends_only() {
			return friends_only;
		}
		public void setFriends_only(Integer friends_only) {
			this.friends_only = friends_only;
		}
		public Comments getComments() {
			return comments;
		}
		public void setComments(Comments comments) {
			this.comments = comments;
		}
		public Likes getLikes() {
			return likes;
		}
		public void setLikes(Likes likes) {
			this.likes = likes;
		}
		public Reposts getReposts() {
			return reposts;
		}
		public void setReposts(Reposts reposts) {
			this.reposts = reposts;
		}
		public String getPost_type() {
			return post_type;
		}
		public void setPost_type(String post_type) {
			this.post_type = post_type;
		}
		public PostSource getPost_source() {
			return post_source;
		}
		public void setPost_source(PostSource post_source) {
			this.post_source = post_source;
		}
		public VKAttachment[] getAttachments() {
			return attachments;
		}
		public void setAttachments(VKAttachment[] attachments) {
			this.attachments = attachments;
		}
		public VKMessage.Geo getGeo() {
			return geo;
		}
		public void setGeo(VKMessage.Geo geo) {
			this.geo = geo;
		}
		public Integer getSigner_id() {
			return signer_id;
		}
		public void setSigner_id(Integer signer_id) {
			this.signer_id = signer_id;
		}
		public Wall[] getCopy_history() {
			return copy_history;
		}
		public void setCopy_history(Wall[] copy_history) {
			this.copy_history = copy_history;
		}
		public Integer getCan_pin() {
			return can_pin;
		}
		public void setCan_pin(Integer can_pin) {
			this.can_pin = can_pin;
		}
		public Integer getCan_delete() {
			return can_delete;
		}
		public void setCan_delete(Integer can_delete) {
			this.can_delete = can_delete;
		}
		public Integer getCan_edit() {
			return can_edit;
		}
		public void setCan_edit(Integer can_edit) {
			this.can_edit = can_edit;
		}
		public Integer getIs_pinned() {
			return is_pinned;
		}
		public void setIs_pinned(Integer is_pinned) {
			this.is_pinned = is_pinned;
		}
		public Integer getMarked_as_ads() {
			return marked_as_ads;
		}
		public void setMarked_as_ads(Integer marked_as_ads) {
			this.marked_as_ads = marked_as_ads;
		}
		public Boolean getIs_favorite() {
			return is_favorite;
		}
		public void setIs_favorite(Boolean is_favorite) {
			this.is_favorite = is_favorite;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getFrom_id() {
			return from_id;
		}
		public void setFrom_id(Integer from_id) {
			this.from_id = from_id;
		}
		public Long getDate() {
			return date;
		}
		public void setDate(Long date) {
			this.date = date;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public Integer getCreated_by() {
			return created_by;
		}
		public void setCreated_by(Integer created_by) {
			this.created_by = created_by;
		}
		public Integer getReply_owner_id() {
			return reply_owner_id;
		}
		public void setReply_owner_id(Integer reply_owner_id) {
			this.reply_owner_id = reply_owner_id;
		}
		public Integer getReply_post_id() {
			return reply_post_id;
		}
		public void setReply_post_id(Integer reply_post_id) {
			this.reply_post_id = reply_post_id;
		}
		public Boolean isCan_pin() {
			return (can_pin==null||can_pin==0)?false:true;
		}
		public Boolean isCan_delete() {
			return (can_delete==null||can_delete==0)?false:true;
		}
		public Boolean isCan_edit() {
			return (can_edit==null||can_edit==0)?false:true;
		}
		public Boolean isPinned() {
			return (is_pinned==null||is_pinned==0)?false:true;
		}
		public Boolean isMarked_as_ads() {
			return (marked_as_ads==null||marked_as_ads==0)?false:true;
		}
		public Copyright getCopyright() {
			return copyright;
		}
		public void setCopyright(Copyright copyright) {
			this.copyright = copyright;
		}
		public Boolean isFavorite() {
			return is_favorite;
		}
		public class Reposts {
			private Integer count;
			private Integer user_reposted;
			public void setCount(Integer count) {
				this.count = count;
			}
			public Integer getCount() {
				return count==null?0:count;
			}
			public Integer getUser_reposted() {
				return user_reposted;
			}
			public void setUser_reposted(Integer user_reposted) {
				this.user_reposted = user_reposted;
			}
		}
		public class Likes {
			private Integer count;
			private Integer user_likes;
			private Integer can_like;
			private Integer can_publish;
			public Integer getUser_likes() {
				return user_likes;
			}
			public void setUser_likes(Integer user_likes) {
				this.user_likes = user_likes;
			}
			public Integer getCan_like() {
				return can_like;
			}
			public void setCan_like(Integer can_like) {
				this.can_like = can_like;
			}
			public Integer getCan_publish() {
				return can_publish;
			}
			public void setCan_publish(Integer can_publish) {
				this.can_publish = can_publish;
			}
			public void setCount(Integer count) {
				this.count = count;
			}
			public Integer getCount() {
				return count==null?0:count;
			}
		}
		public class Comments {
			private Integer count;
			private Integer can_post;
			public void setCount(Integer count) {
				this.count = count;
			}
			public Integer getCount() {
				return count==null?0:count;
			}
			public Integer getCan_post() {
				return can_post;
			}
			public void setCan_post(Integer can_post) {
				this.can_post = can_post;
			}
			/*
			private Integer groups_can_post;
			public Integer getGroups_can_post() {
				return groups_can_post;
			}
			public void setGroups_can_post(Integer groups_can_post) {
				this.groups_can_post = groups_can_post;
			}
			 */
		}
		public Boolean isFriends_only() {
			return (friends_only==null||friends_only==0)?false:true;
		}
		public void setFriends_only(Boolean isFriends_only) {
			this.friends_only=isFriends_only?1:0;
		}
		public Integer getOwner_id() {
			if(to_id!=null)
				return to_id;
			return owner_id;
		}
		public void setOwner_id(Integer owner_id) {
			if(to_id!=null) to_id=owner_id;
			this.owner_id=owner_id;
		}
		public String toStringAttachment() {
			return "wall"+owner_id+"_"+id+"_"+access_key;
		}
	}
	public class Link {
		private String url;
		private String title;
		private String caption;
		private String description;
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getCaption() {
			return caption;
		}
		public void setCaption(String caption) {
			this.caption = caption;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
	public class Video {
		private String title;
		private String description;
		private String access_key;
		private Integer id;
		private Integer owner_id;
		public String getPlayer() {
			return "https://vk.com/video?z=video"+this.owner_id+"_"+this.id+"_"+access_key;
		}
		public Integer getOwner_id() {
			return owner_id;
		}
		public void setOwner_id(Integer ownder_id) {
			this.owner_id = ownder_id;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String toStringAttachment() {
			return "video"+owner_id+"_"+id+"_"+access_key;
		}
	}
	public class Graffiti {
		private Integer id;
		private Integer owner_id;
		private Integer width;
		private Integer height;
		private String url;
		private String access_key;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public Integer getHeight() {
			return height;
		}
		public void setHeight(Integer height) {
			this.height = height;
		}
		public Integer getWidth() {
			return width;
		}
		public void setWidth(Integer width) {
			this.width = width;
		}
		public Integer getOwner_id() {
			return owner_id;
		}
		public void setOwner_id(Integer owner_id) {
			this.owner_id = owner_id;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getAccess_key() {
			return access_key;
		}
		public void setAccess_key(String access_key) {
			this.access_key = access_key;
		}
	}
	public class Doc {
		private Integer id;
		private Integer owner_id;
		private String access_key;
		private String title;
		private Integer size;
		private String ext;
		private Long date;
		private Integer type;
		private String url;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public Long getDate() {
			return date;
		}
		public void setDate(Long date) {
			this.date = date;
		}
		public String getExt() {
			return ext;
		}
		public void setExt(String ext) {
			this.ext = ext;
		}
		public Integer getSize() {
			return size;
		}
		public void setSize(Integer size) {
			this.size = size;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public Integer getOwner_id() {
			return owner_id;
		}
		public void setOwner_id(Integer owner_id) {
			this.owner_id = owner_id;
		}
		public String getAccess_key() {
			return access_key;
		}
		public void setAccess_key(String access_key) {
			this.access_key = access_key;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String toStringAttachment() {
			return "doc"+owner_id+"_"+id+"_"+access_key;
		}
	}
	public class Sticker {
		private Integer product_id;
		private Integer sticker_id;
		private Image[] images;
		private ImagesWithBackground[] images_with_background;
		public ImagesWithBackground getMaxImageWithBackground() {
			Integer count = this.getImages_with_background().length;
			if(count<1) {
				return null;
			} else {
				return images_with_background[count-1];
			}
		}
		public class ImagesWithBackground {
			private String url;
			private String width;
			private String height;
			public String getHeight() {
				return height;
			}
			public void setHeight(String height) {
				this.height = height;
			}
			public String getWidth() {
				return width;
			}
			public void setWidth(String width) {
				this.width = width;
			}
			public String getUrl() {
				return url;
			}
			public void setUrl(String url) {
				this.url = url;
			}			
		}
		public class Image {
			private String url;
			private String width;
			private String height;
			public String getHeight() {
				return height;
			}
			public void setHeight(String height) {
				this.height = height;
			}
			public String getWidth() {
				return width;
			}
			public void setWidth(String width) {
				this.width = width;
			}
			public String getUrl() {
				return url;
			}
			public void setUrl(String url) {
				this.url = url;
			}
		}
		public Image[] getImages() {
			return images;
		}
		public void setImages(Image[] images) {
			this.images = images;
		}
		public Integer getSticker_id() {
			return sticker_id;
		}
		public void setSticker_id(Integer sticker_id) {
			this.sticker_id = sticker_id;
		}
		public Integer getProduct_id() {
			return product_id;
		}
		public void setProduct_id(Integer product_id) {
			this.product_id = product_id;
		}
		public ImagesWithBackground[] getImages_with_background() {
			return images_with_background;
		}
		public void setImages_with_background(ImagesWithBackground[] images_with_background) {
			this.images_with_background = images_with_background;
		}
	}
	public class Audio_message {
		private Integer id;
		private Integer owner_id;
		private Integer duration;
		private String link_ogg;
		private String link_mp3;
		private String access_key;
		private String transcript;
		private String transcript_state;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getDuration() {
			return duration;
		}
		public void setDuration(Integer duration) {
			this.duration = duration;
		}
		public String getLink_ogg() {
			return link_ogg;
		}
		public void setLink_ogg(String link_ogg) {
			this.link_ogg = link_ogg;
		}
		public String getLink_mp3() {
			return link_mp3;
		}
		public void setLink_mp3(String link_mp3) {
			this.link_mp3 = link_mp3;
		}
		public String getAccess_key() {
			return access_key;
		}
		public void setAccess_key(String access_key) {
			this.access_key = access_key;
		}
		public Integer getOwner_id() {
			return owner_id;
		}
		public void setOwner_id(Integer owner_id) {
			this.owner_id = owner_id;
		}
		public String getTranscript_state() {
			return transcript_state;
		}
		public void setTranscript_state(String transcript_state) {
			this.transcript_state = transcript_state;
		}
		public String getTranscript() {
			return transcript;
		}
		public void setTranscript(String transcript) {
			this.transcript = transcript;
		}
	}
	public class Audio {
		private String artist;
		private Integer id;
		private Integer owner_id;
		private String access_key;
		private String title;
		private Integer duration;
		private Boolean is_explicit;
		private Boolean is_focus_track;
		private String track_code;
		private String url;
		private Long date;
		private Integer lyrics_id;
		private Integer genre_id;
		private Boolean short_videos_allowed;
		private Boolean stories_allowed;
		public String getArtist() {
			return artist;
		}
		public void setArtist(String artist) {
			this.artist = artist;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getOwner_id() {
			return owner_id;
		}
		public void setOwner_id(Integer owner_id) {
			this.owner_id = owner_id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public Integer getDuration() {
			return duration;
		}
		public void setDuration(Integer duration) {
			this.duration = duration;
		}
		public Boolean getIs_focus_track() {
			return is_focus_track;
		}
		public void setIs_focus_track(Boolean is_focus_track) {
			this.is_focus_track = is_focus_track;
		}
		public Boolean getIs_explicit() {
			return is_explicit;
		}
		public void setIs_explicit(Boolean is_explicit) {
			this.is_explicit = is_explicit;
		}
		public String getTrack_code() {
			return track_code;
		}
		public void setTrack_code(String track_code) {
			this.track_code = track_code;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public Long getDate() {
			return date;
		}
		public void setDate(Long date) {
			this.date = date;
		}
		public Integer getLyrics_id() {
			return lyrics_id;
		}
		public void setLyrics_id(Integer lyrics_id) {
			this.lyrics_id = lyrics_id;
		}
		public Integer getGenre_id() {
			return genre_id;
		}
		public void setGenre_id(Integer genre_id) {
			this.genre_id = genre_id;
		}
		public Boolean getShort_videos_allowed() {
			return short_videos_allowed;
		}
		public void setShort_videos_allowed(Boolean short_videos_allowed) {
			this.short_videos_allowed = short_videos_allowed;
		}
		public Boolean getStories_allowed() {
			return stories_allowed;
		}
		public void setStories_allowed(Boolean stories_allowed) {
			this.stories_allowed = stories_allowed;
		}
		public String getAccess_key() {
			return access_key;
		}
		public void setAccess_key(String access_key) {
			this.access_key = access_key;
		}
		public String toStringAttachment() {
			return "audio"+owner_id+"_"+id+"_"+access_key;
		}
	}
	public class Photo {
		private Integer album_id;
		private Long date;
		private Integer id;
		private Integer owner_id;
		private Boolean has_tags;
		private String access_key;
		private String text;
		private Size[] sizes;
		public String toStringAttachment() {
			return "photo"+owner_id+"_"+id+"_"+access_key;
		}
		public class Size {
			private Integer width;
			private Integer height;
			private String type;
			private String url;
			public String getUrl() {
				return url;
			}
			public void setUrl(String url) {
				this.url = url;
			}
			public String getType() {
				return type;
			}
			public void setType(String type) {
				this.type = type;
			}
			public Integer getHeight() {
				return height;
			}
			public void setHeight(Integer height) {
				this.height = height;
			}
			public Integer getWidth() {
				return width;
			}
			public void setWidth(Integer width) {
				this.width = width;
			}
		}
		public Size getSize(String pType) {
			for(Size size : sizes) {
				if(size.getType().equals(pType)) {
					return size;
				}
			}
			return null;
		}		
		public Size[] getSizes() {
			return sizes;
		}
		public void setSizes(Size[] sizes) {
			this.sizes = sizes;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getAccess_key() {
			return access_key;
		}
		public void setAccess_key(String access_key) {
			this.access_key = access_key;
		}
		public Boolean getHas_tags() {
			return has_tags;
		}
		public void setHas_tags(Boolean has_tags) {
			this.has_tags = has_tags;
		}
		public Integer getOwner_id() {
			return owner_id;
		}
		public void setOwner_id(Integer owner_id) {
			this.owner_id = owner_id;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Long getDate() {
			return date;
		}
		public void setDate(Long date) {
			this.date = date;
		}
		public Integer getAlbum_id() {
			return album_id;
		}
		public void setAlbum_id(Integer album_id) {
			this.album_id = album_id;
		}
		public Size getMaxSize() {
			Integer maxHeigth=0;
			String pType=null;
			for(Size size : sizes) {
				//System.out.print("F: "+size.getWidth()+"x"+size.getHeight()+" ");
				if(size.getHeight()>=maxHeigth) {
					//System.out.print("NEW MAX!");
					pType=size.getType();
					maxHeigth=size.getHeight();
				}
				//System.out.println();
			}
			if(pType!=null) {
				return this.getSize(pType);
			} else {
				return null;
			}
		}
	}
	public Wall getWall() { return wall; }
	public void setWall(Wall wall) { this.wall = wall; }
	public Sticker getSticker() {
		return sticker;
	}
	public void setSticker(Sticker sticker) {
		this.sticker = sticker;
	}
	public Audio_message getAudio_message() {
		return audio_message;
	}
	public void setAudio_message(Audio_message audio_message) {
		this.audio_message = audio_message;
	}
	public Audio getAudio() {
		return audio;
	}
	public void setAudio(Audio audio) {
		this.audio = audio;
	}
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Doc getDoc() {
		return doc;
	}
	public void setDoc(Doc doc) {
		this.doc = doc;
	}
	public Graffiti getGraffiti() {
		return graffiti;
	}
	public void setGraffiti(Graffiti graffiti) {
		this.graffiti = graffiti;
	}
	public Video getVideo() {
		return video;
	}
	public void setVideo(Video video) {
		this.video = video;
	}
	public Link getLink() {
		return link;
	}
	public void setLink(Link link) {
		this.link = link;
	}
}
