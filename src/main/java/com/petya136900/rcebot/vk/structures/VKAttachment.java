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
		private Integer id;
		private Integer owner_id;
		private String access_key;
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
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
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
