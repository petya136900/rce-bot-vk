package com.petya136900.rcebot.vk.structures;

import com.google.gson.annotations.SerializedName;
import com.petya136900.rcebot.vk.structures.VKAttachment.Photo;

public class User {
	public enum name_case {
		/**
		 * именительный
		 */
		nom,
		/**
		 * родительный
		 */
		gen,
		/**
		 * дательный
		 */
		dat, 
		/**
		 * винительный
		 */
		acc, 
		/**
		 * творительный
		 */
		ins,
		/**
		 * предложный
		 */
		abl 
	}
	public enum deactivated {
		deleted,
		banned
	}
	private Integer id;
	private String first_name;
	private String last_name;
	private Boolean can_access_closed;
	private Boolean is_closed;
	private LastSeen last_seen;
	private String about;
	private String activities;
	private String bdate;
	private Integer blacklisted;
	private Integer blacklisted_by_me;
	private String books;
	private Integer can_see_all_posts;
	private Integer can_see_audio;
	private Integer can_send_friend_request;
	private Integer can_write_private_message;
	private Career[] career;
	public class Career {
		private Integer group_id;
		public Integer getGroup_id() {
			return group_id;
		}
		public String getCompany() {
			return company;
		}
		public Integer getCountry_id() {
			return country_id;
		}
		public Integer getCity_id() {
			return city_id;
		}
		public String getCity_name() {
			return city_name;
		}
		public Integer getFrom() {
			return from;
		}
		public Integer getUntil() {
			return until;
		}
		public String getPosition() {
			return position;
		}
		private String company;
		private Integer country_id;
		private Integer city_id;
		private String city_name;
		private Integer from;
		private Integer until;
		private String position;
	}
	private City city;
	public class City {
		private Integer id;
		private String title;
		public String getTitle() {
			return title;
		}
		public Integer getId() {
			return id;
		}
	}
	private Integer common_count;
	private String skype;
	private String facebook;
	private String twitter;
	public Integer getId() {
		return id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public Boolean getCan_access_closed() {
		return can_access_closed;
	}
	public LastSeen getLast_seen() {
		return last_seen;
	}
	public String getAbout() {
		return about;
	}
	public String getActivities() {
		return activities;
	}
	public String getBdate() {
		return bdate;
	}
	public Integer getBlacklisted_by_me() {
		return blacklisted_by_me;
	}
	public String getBooks() {
		return books;
	}
	public Integer getCan_see_all_posts() {
		return can_see_all_posts;
	}
	public Integer getCan_see_audio() {
		return can_see_audio;
	}
	public Integer getCan_send_friend_request() {
		return can_send_friend_request;
	}
	public Integer getCan_write_private_message() {
		return can_write_private_message;
	}
	public Career[] getCareer() {
		return career;
	}
	public City getCity() {
		return city;
	}
	public Integer getCommon_count() {
		return common_count;
	}
	public String getSkype() {
		return skype;
	}
	public String getFacebook() {
		return facebook;
	}
	public String getTwitter() {
		return twitter;
	}
	public String getLivejournal() {
		return livejournal;
	}
	public String getInstagram() {
		return instagram;
	}
	public Contacts getContacts() {
		return contacts;
	}
	public Counters getCounters() {
		return counters;
	}
	private String livejournal;
	private String instagram;
	private Contacts contacts;
	public class Contacts {
		private String mobile_phone;
		private String home_phone;
		public String getHome_phone() {
			return home_phone;
		}
		public String getMobile_phone() {
			return mobile_phone;
		}
	}
	private Counters counters;
	public class Counters {
		public Integer getAlbums() {
			return albums;
		}
		public Integer getVideos() {
			return videos;
		}
		public Integer getAudios() {
			return audios;
		}
		public Integer getPhotos() {
			return photos;
		}
		public Integer getNotes() {
			return notes;
		}
		public Integer getFriends() {
			return friends;
		}
		public Integer getGroups() {
			return groups;
		}
		public Integer getOnline_friends() {
			return online_friends;
		}
		public Integer getFollowers() {
			return followers;
		}
		private Integer albums;
		private Integer videos;
		private Integer audios;
		private Integer photos;
		private Integer notes;
		private Integer friends;
		private Integer groups;
		private Integer online_friends;
		private Integer mutual_friends;
		private Integer user_videos;
		private Integer followers;
		private Integer pages;
		public Integer getPages() {
			return pages;
		}
		public Integer getUser_videos() {
			return user_videos;
		}
		public Integer getMutual_friends() {
			return mutual_friends;
		}
	}
	private Country country;
	public class Country {
		private Integer id;
		private String title;
		public String getTitle() {
			return title;
		}
		public Integer getId() {
			return id;
		}
	}
	private CropPhoto crop_photo;
	public class CropPhoto {
		private Photo photo;
		private Crop crop;
		public class Crop {
			private double x;
			private double y;
			private double x2;
			private double y2;
			public double getY2() {
				return y2;
			}
			public double getX2() {
				return x2;
			}
			public double getX() {
				return x;
			}
			public double getY() {
				return y;
			}
		}
		private Rect rect;
		public class Rect {
			private double x;
			private double y;
			private double x2;
			private double y2;
			public double getY2() {
				return y2;
			}
			public double getX2() {
				return x2;
			}
			public double getY() {
				return y;
			}
			public double getX() {
				return x;
			}
		}
		public Rect getRect() {
			return rect;
		}
		public Crop getCrop() {
			return crop;
		}
		public Photo getPhoto() {
			return photo;
		}
	}
	private String domain;
	private Integer university;
	private String university_name;
	private Integer faculty;
	private String faculty_name;
	private Integer graduation;
	private Integer followers_count;
	private Integer friend_status;
	private String games;
	private Integer has_mobile;
	private Integer has_photo;
	private String home_town;
	private String interests;
	private Integer is_favorite;
	private Integer is_friend;
	private Integer is_hidden_from_feed;
	private String lists;
	private String maiden_name;
	private Military[] military;
	public class Military {
		private String unit;
		private Integer unit_id;
		private Integer country_id;
		private Integer from;
		public String getUnit() {
			return unit;
		}
		public Integer getUnit_id() {
			return unit_id;
		}
		public Integer getCountry_id() {
			return country_id;
		}
		public Integer getFrom() {
			return from;
		}
		public Integer getUntil() {
			return until;
		}
		private Integer until;
	}
	private String movies;
	private String music;
	private String nickname;
	private Occupation occupation;
	public class Occupation {
		private OccupationType type;
		private Integer id;
		private String name;
		public OccupationType getType() {
			return type;
		}
		public Integer getId() {
			return id;
		}
		public String getName() {
			return name;
		}
	}
	private Integer online;
	private Personal personal;
	public Personal getPersonal() {
		return personal;
	}
	public class Personal {
		private Integer political;
		private String[] langs;
		private String religion;
		private String inspired_by;
		private Integer people_main;
		private Integer life_main;
		private Integer smoking;
		private Integer alcohol;
		public Integer getPolitical() {
			return political;
		}
		public String[] getLangs() {
			return langs;
		}
		public String getReligion() {
			return religion;
		}
		public String getInspired_by() {
			return inspired_by;
		}
		public Integer getPeople_main() {
			return people_main;
		}
		public Integer getLife_main() {
			return life_main;
		}
		public Integer getSmoking() {
			return smoking;
		}
		public Integer getAlcohol() {
			return alcohol;
		}
	}
	private String photo_50;
	private String photo_100;
	private String photo_200_orig;
	private String photo_200;
	private String photo_400_orig;
	private String photo_max;
	private String photo_max_orig;
	private String quotes;
	private Relative[] relatives;
	public Relative[] getRelatives() {
		return relatives;
	}
	public class Relative {
		private Integer id;
		private String name;
		public Integer getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public String getType() {
			return type;
		}
		private String type;
	}
	private Integer relation;
	private RelationPartner relation_partner;
	public class RelationPartner {
		private String first_name;
		private String last_name;
		public String getFirst_name() {
			return first_name;
		}
		public String getLast_name() {
			return last_name;
		}
		public Integer getId() {
			return id;
		}
		private Integer id;
	}
	private School[] schools;
	public class School {
		private Integer id;
		private Integer county;
		private Integer city;
		private String name;
		private Integer year_from;
		private Integer year_to;
		private Integer year_graduated;
		@SerializedName(value = "class")
		private String _class;
		private String speciality;
		private Integer type;
		public Integer getId() {
			return id;
		}
		public Integer getCounty() {
			return county;
		}
		public Integer getCity() {
			return city;
		}
		public String getName() {
			return name;
		}
		public Integer getYear_from() {
			return year_from;
		}
		public Integer getYear_to() {
			return year_to;
		}
		public Integer getYear_graduated() {
			return year_graduated;
		}
		public String get_class() {
			return _class;
		}
		public String getSpeciality() {
			return speciality;
		}
		public Integer getType() {
			return type;
		}
		public String getType_str() {
			return type_str;
		}
		private String type_str;
	}
	private String screen_name;
	private Integer sex;
	private String site;
	private String status;
	private Integer timezone;
	private Integer trending;
	private String tv;
	private University[] universities;
	public class University {
		private Integer id;
		private Integer county;
		private Integer city;
		private String name;
		private Integer faculty;
		private String faculty_name;
		private Integer chair;
		private String chair_name;
		private Integer graduation;
		private String education_form;
		private String education_status;
		public Integer getId() {
			return id;
		}
		public Integer getCounty() {
			return county;
		}
		public Integer getCity() {
			return city;
		}
		public String getName() {
			return name;
		}
		public Integer getFaculty() {
			return faculty;
		}
		public String getFaculty_name() {
			return faculty_name;
		}
		public Integer getChair() {
			return chair;
		}
		public String getChair_name() {
			return chair_name;
		}
		public Integer getGraduation() {
			return graduation;
		}
		public String getEducation_form() {
			return education_form;
		}
		public String getEducation_status() {
			return education_status;
		}
	}
	private Integer verified;
	public String getMaiden_name() {
		return maiden_name;
	}
	public Military[] getMilitary() {
		return military;
	}
	public String getMovies() {
		return movies;
	}
	public String getMusic() {
		return music;
	}
	public String getNickname() {
		return nickname;
	}
	public Occupation getOccupation() {
		return occupation;
	}
	/**
	 * 0,1 
	 */
	public Integer getOnline() {
		return online;
	}
	public String getPhoto_50() {
		return photo_50;
	}
	public String getPhoto_100() {
		return photo_100;
	}
	public String getPhoto_200_orig() {
		return photo_200_orig;
	}
	public String getPhoto_200() {
		return photo_200;
	}
	public String getPhoto_400_orig() {
		return photo_400_orig;
	}
	public String getPhoto_max() {
		return photo_max;
	}
	public String getPhoto_max_orig() {
		return photo_max_orig;
	}
	public String getQuotes() {
		return quotes;
	}
	public Integer getRelation() {
		return relation;
	}
	public RelationPartner getRelation_partner() {
		return relation_partner;
	}
	public School[] getSchools() {
		return schools;
	}
	public String getScreen_name() {
		return screen_name;
	}
	/**
	 * 0 - none, 1 - woman, 2 - man 
	 */
	public Integer getSex() {
		return sex;
	}
	public String getSite() {
		return site;
	}
	public String getStatus() {
		return status;
	}
	public Integer getTimezone() {
		return timezone;
	}
	public Integer getTrending() {
		return trending;
	}
	public String getTv() {
		return tv;
	}
	public University[] getUniversities() {
		return universities;
	}
	/**
	 * 0,1 
	 */
	public Integer getVerified() {
		return verified;
	}
	/**
	 * owner, all 
	 */
	public String getWall_default() {
		return wall_default;
	}
	private String wall_default;
	public class LastSeen {
		/**
		 *  1 - Mobile web, 2 - iPhone, <br>
		 *  3 - iPad, 4 - Android, <br>
		 *  5 - Windows Phone, 6 - Windows 10, <br> 
		 *  7 - WEB 
		 */
		public Integer getPlatform() {
			return platform;
		}
		public Long getTime() {
			return time;
		}
		private Integer platform; 
		private Long time;
	}
	public Integer getBlacklisted() {
		return blacklisted;
	}
	public Boolean getIs_closed() {
		return is_closed;
	}
	public Integer getIs_hidden_from_feed() {
		return is_hidden_from_feed;
	}
	public String getLists() {
		return lists;
	}
	public Integer getIs_friend() {
		return is_friend;
	}
	public Integer getIs_favorite() {
		return is_favorite;
	}
	public String getInterests() {
		return interests;
	}
	public Integer getHas_photo() {
		return has_photo;
	}
	public String getHome_town() {
		return home_town;
	}
	public Integer getHas_mobile() {
		return has_mobile;
	}
	public String getGames() {
		return games;
	}
	public Integer getFriend_status() {
		return friend_status;
	}
	public Integer getFollowers_count() {
		return followers_count;
	}
	public Integer getGraduation() {
		return graduation;
	}
	public String getFaculty_name() {
		return faculty_name;
	}
	public Integer getFaculty() {
		return faculty;
	}
	public String getUniversity_name() {
		return university_name;
	}
	public Integer getUniversity() {
		return university;
	}
	public String getDomain() {
		return domain;
	}
	public CropPhoto getCrop_photo() {
		return crop_photo;
	}
	public Country getCountry() {
		return country;
	}
}
