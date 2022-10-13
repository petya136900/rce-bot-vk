package com.petya136900.rcebot.web;


import com.petya136900.rcebot.tools.Properties;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petya136900.rcebot.lifecycle.Logger;
import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.handlers.TeacherHandler;
import com.petya136900.rcebot.rce.teachers.Teacher;
import com.petya136900.rcebot.rce.teachers.TeacherException;
import com.petya136900.rcebot.rce.teachers.TeacherPair;
import com.petya136900.rcebot.rce.teachers.TeachersData;
import com.petya136900.rcebot.rce.teachers.TeacherException.Type;
import com.petya136900.rcebot.rce.timetable.MainShelude;
import com.petya136900.rcebot.rce.timetable.MainsheludeData;
import com.petya136900.rcebot.rce.timetable.PairsData;
import com.petya136900.rcebot.rce.timetable.TimetableClient;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.tools.JsonParser;

@RestController
public class WebController {
	private static final String PASSWORD=Properties.getProperty("WEB_ADMIN_PASSWORD");;
	private static String groupsAllHash=null;
	private static MainShelude[] mains=null;
	
	private static String teachersHash=null;
	private static Teacher[] teachers=null;
	
	private static String pairsHash=null;
	private static TeacherPair[] pairs=null;
	
	@RequestMapping(value = "/mainshelude", method = RequestMethod.GET)
	public String mainsheludeController(@RequestParam(value = "type", required = true, defaultValue = "none") String type, 
										@RequestParam(value = "hash", required = false, defaultValue = "none") String hash) {
			if(type.equals("none")) {
				try {
					updateHash();
				} catch(TimetableException te) {
					//te.printStackTrace();
					return "{\"error\":\"true\"}";
				}
				return "{"
							+ "\"error\":\"false\","
							+ "\"response\":"+JsonParser.toJson(mains)+","
							+ "\"groupsHash\":"+"\""+groupsAllHash+"\""
					+ "}";
			} else if(type.equals("bs")) {
				try {
					return "{\"error\":\"false\",\"response\":"+JsonParser.toJson(MySqlConnector.getBotSettings())+"}";
				} catch (TimetableException e) {
					return "{\"error\":\"true\"}";
				}
			} else if(type.equals("gh")) {
				try {
					updateHash();
					return "{"
								+ "\"error\":\"false\","
								+ "\"groupsHash\":"+"\""+groupsAllHash+"\""
							+ "}";				
				} catch(TimetableException te) {
					//te.printStackTrace();
					return "{\"error\":\"true\"}";
				}
			} else if(type.equals("cu")) {
				try {
					updateHash();
					if(!(hash.equals(groupsAllHash))) {
						return "{"
								+ "\"error\":\"false\","
								+ "\"update\":\"true\","
								+ "\"groupsHash\":"+"\""+groupsAllHash+"\""
							+ "}";	
					}
					return "{"
							+ "\"error\":\"false\","
							+ "\"update\":\"false\""
						+ "}";					
				} catch(TimetableException te) {
					return "{\"error\":\"true\"}";
				}
			} else {
				return "{\"error\":\"true\"}";
			}
	}	
	private void updateHash() throws TimetableException {
		if(mains==null) {
			mains = MySqlConnector.getMainShelude();
		}
		if(groupsAllHash==null) {
			groupsAllHash=getGroupHash(mains);
		}
	}
	private void updateTeachersHash(Boolean force) throws TimetableException {
		if((teachers==null)|force) {
			teachers = MySqlConnector.getTeachers();
		}
		if((teachersHash==null)|force) {
			teachersHash=getTeachersHash(teachers);
		}
	}
	private void updatePairsHash(Boolean force) throws TimetableException {
		if((pairs==null)|force) {
			pairs = MySqlConnector.getTeachersPairs();
		}
		if((pairsHash==null)|force) {
			pairsHash=getPairsHash(pairs);
		}
	}	
	synchronized private String getPairsHash(TeacherPair... pairs) {
		StringBuilder sb=new StringBuilder("");
		for(TeacherPair pair : pairs) {
			if(pair.getHash()==null) {
				pair.updateHash();
			}
			sb.append(pair.getHash());
		} 	
		return DigestUtils.md5Hex(sb.toString());	
	}
	synchronized private String getGroupHash(MainShelude... mains) {
		StringBuilder sb=new StringBuilder("");
		for(MainShelude main : mains) {
			if(main.getHash()==null) {
				main.updateHash();
			}
			sb.append(main.getHash());
		} 	
		return DigestUtils.md5Hex(sb.toString());
	}
	synchronized private String getTeachersHash(Teacher... teachers) {
		StringBuilder sb=new StringBuilder("");
		for(Teacher teacher : teachers) {
			if(teacher.getHash()==null) {
				teacher.updateHash();
			}
			sb.append(teacher.getHash());
		} 	
		return DigestUtils.md5Hex(sb.toString());	
	}
	public class GroupName {
		private String groupName;
		public String getGroupName() {
			return groupName;
		}
	}
	@RequestMapping(value = "/api/groupname", method = RequestMethod.GET)
	public String groupnameController(@RequestParam(value = "peer_id", required = true, defaultValue = "none") String peer_id) {
		if(peer_id.equals("none")) {
			return JsonParser.toJson(WebResponse.createError("set_arg").setDesc("peer_id can't be null"));
		}
		Integer peer_id_i=null;
		try {
			peer_id_i = Integer.parseInt(peer_id);
			try {
				String groupName = MySqlConnector.getGroupNameByPeerID(peer_id_i);
				return JsonParser.toJson(WebResponse.createGroup(groupName));
			} catch (Exception e) {
				return JsonParser.toJson(WebResponse.createError("mysql_error").setDesc("Something is wrong with the MySql server"));	
			}
		} catch(Exception e) {
			return JsonParser.toJson(WebResponse.createError("bad_arg").setDesc("peer_id must be Integer"));
		}
	}
	@RequestMapping(value = "/teachers", method = RequestMethod.GET)
	public String teachersController(@RequestParam(value = "type", required = true, defaultValue = "none") String type) {
		try {
			WebResponse response;
			switch(type) {
				case("hash"):
					updateTeachersHash(false);
					response = WebResponse.create("ok");
					response.setHash(teachersHash);
					return response.toJson();
				case("getall"):
					updateTeachersHash(true);
					response =  WebResponse.create("ok");
					response.setHash(teachersHash);
					response.setTeachers(teachers);
					return response.toJson();
				default:
					throw new TeacherException(Type.UNKNOWN_ACTION);
			}
		}	catch (TeacherException te) {
			System.out.println("Exception gotcha! : "+te.getType().toString());
				switch(te.getType().toString()) {
					default:
						return WebResponse.createError(null).toJson();
				}
			} catch(Exception e) {
				e.printStackTrace();
				return WebResponse.createError(null).toJson();
			}
	}
	@RequestMapping(value = "/pairs", method = RequestMethod.GET)
	public String pairsController(@RequestParam(value = "type", required = true, defaultValue = "none") String type) {
		try {
			WebResponse response;
			switch(type) {
				case("hash"):
					updatePairsHash(false);
					response = WebResponse.create("ok");
					response.setHash(pairsHash);
					return response.toJson();
				case("getall"):
					updatePairsHash(true);
					response =  WebResponse.create("ok");
					response.setHash(pairsHash);
					response.setPairs(pairs);
					return response.toJson();
				default:
					throw new TeacherException(Type.UNKNOWN_ACTION);
			}
		}	catch (TeacherException te) {
			System.out.println("Exception gotcha! : "+te.getType().toString());
				switch(te.getType().toString()) {
					default:
						return WebResponse.createError(null).toJson();
				}
			} catch(Exception e) {
				e.printStackTrace();
				return WebResponse.createError(null).toJson();
			}
	}
	@RequestMapping(value = "/pairs", method = RequestMethod.POST)
	public String pairsController(@RequestParam(value = "type", required = true, defaultValue = "none") String type,
			@RequestBody(required = false) String jsonRequest) {
		TeachersData td = null;
		try {
			//System.out.println(jsonRequest);
			checkIsJson(jsonRequest);
			switch(type) {
				case("add"):
					td = JsonParser.fromJson(jsonRequest, TeachersData.class);
					checkPassword(td.getPassword());					
					Integer teacherId=null;
					try {
						teacherId=Integer.parseInt(td.getId().trim());
					} catch(Exception e) {
						throw new TeacherException(Type.BAD_ID);
					}
					if(teacherId>0) {
						MySqlConnector.addPairToTeacher(teacherId,td.getName());
					} else {
						TeacherPair tp = new TeacherPair(TeacherHandler.removeTrash(td.getName()));
						if(tp.getName().trim().length()<1) {
							throw new TeacherException(Type.EMPTY_NAME);
						}
						MySqlConnector.addNewUnknownPairs(new TeacherPair[] {tp});
					}
					updatePairsHash(true);
				return WebResponse.create("added").toJson();
				case("update"):
					PairsData pd = JsonParser.fromJson(jsonRequest, PairsData.class);
					checkPassword(pd.getPassword());
					switch(pd.getType()) {
						case("delete"):
							deletePair(pd);
							updatePairsHash(true);
							Logger.printInfo("Pairs | Pair deleted updated | "+pd.getIp());
							return WebResponse.create("deleted").toJson(); 
						case("both"):
							updatePairBoth(pd);
							updatePairsHash(true);
							Logger.printInfo("Pairs | Name and Teacher updated | "+pd.getIp());
							return WebResponse.create("both").toJson();
						case("name"):
							updatePairName(pd);
							updatePairsHash(true);
							Logger.printInfo("Pairs | Name updated | "+pd.getIp());
							return WebResponse.create("name").toJson();
						case("tid"):
							updatePairTid(pd);
							updatePairsHash(true);
							Logger.printInfo("Pairs | Teacher updated | "+pd.getIp());
							return WebResponse.create("tid").toJson();
						default:
							throw new TeacherException(Type.UNKNOWN_ACTION);
					}
				default:
					throw new TeacherException(Type.UNKNOWN_ACTION);
			}
		} catch (TeacherException te) {
			Logger.printInfo("Pairs | Error | "+te.getType().toString()+"\nIP: "+(td!=null?td.getIp():"null"));
			System.out.println("Exception gotcha! : "+te.getType().toString());
			switch(te.getType().toString()) {
				case("BAD_PASSWORD"):
					try {
						Thread.sleep(3500);
					} catch (InterruptedException ignored) {
					}
					return WebResponse.createError("badpass").toJson();
				case("ALREADY_ADDED"):
					return WebResponse.createError("already").toJson();
				case("ALREADY_UNKNOWN"):
					return WebResponse.createError("already_unkwn").toJson();
				case("BAD_ID"):
					return WebResponse.createError("badid").toJson();
				case("BAD_PAIR_ID"):
					return WebResponse.createError("badpid").toJson();
				case("BUSY_BY"):
					WebResponse errorResponse = WebResponse.createError("busyby");
					errorResponse.setDesc(te.getTeacher().getFullName());
					return errorResponse.toJson();
				case("EMPTY_NAME"):
					return WebResponse.createError("empty").toJson();
				default:
					return WebResponse.createError(null).toJson();
			}
		} catch(Exception e) {
			return WebResponse.createError(null).toJson();
		}
	}
	private void deletePair(PairsData pd) throws TeacherException, TimetableException {
		Integer pid=null;
		try {
			pid=Integer.parseInt(pd.getPairID());
		} catch (Exception e) {
			throw new TeacherException(Type.BAD_PAIR_ID);
		}		
		TeacherPair tp = MySqlConnector.getPairById(pid);	
		if(tp.getTeacherID()>0) {
			MySqlConnector.getTeacher(tp.getTeacherID());
			MySqlConnector.removePairById(tp.getId()+"");
			MySqlConnector.addNewUnknownPairs(new TeacherPair[] {
				new TeacherPair(tp.getName())
			});
		} else {
			MySqlConnector.removePairById(pd.getPairID());
		}
	}
	private void updatePairBoth(PairsData pd) throws TeacherException, TimetableException {
		String pairName=TeacherHandler.removeTrash(pd.getPairName());
		if(pairName.length()<1) {
			throw new TeacherException(Type.EMPTY_NAME);
		}
		Integer ptid=null;
		Integer pid=null;
		try {
			pid=Integer.parseInt(pd.getPairID());
		} catch (Exception e) {
			throw new TeacherException(Type.BAD_PAIR_ID);
		}		
		TeacherPair tp = MySqlConnector.getPairById(pid);
		try {
			ptid=Integer.parseInt(pd.getPairTeacherID());
		} catch (Exception e) {
			throw new TeacherException(Type.BAD_ID);
		}
		if(ptid>0) {
			MySqlConnector.getTeacher(ptid);
			MySqlConnector.addPairToTeacher(ptid, pairName);
		} else {
			MySqlConnector.addNewUnknownPairs(new TeacherPair[] {
					new TeacherPair(tp.getName())
			});
		}
		MySqlConnector.removePairById(pd.getPairID());	
	}
	private void updatePairTid(PairsData pd) throws TeacherException, TimetableException {
		Integer ptid=null;
		Integer pid=null;
		try {
			pid=Integer.parseInt(pd.getPairID());
		} catch (Exception e) {
			throw new TeacherException(Type.BAD_PAIR_ID);
		}		
		TeacherPair tp = MySqlConnector.getPairById(pid);
		try {
			ptid=Integer.parseInt(pd.getPairTeacherID());
		} catch (Exception e) {
			throw new TeacherException(Type.BAD_ID);
		}
		if(ptid>0) {
			MySqlConnector.addPairToTeacher(ptid, tp.getName());
		} else {
			MySqlConnector.addNewUnknownPairs(new TeacherPair[] {
					new TeacherPair(tp.getName())
			});
		}
		MySqlConnector.removePairById(pd.getPairID());		
	}
	private void updatePairName(PairsData pd) throws TeacherException, TimetableException {
		String pairName=TeacherHandler.removeTrash(pd.getPairName());
		if(pairName.length()<1) {
			throw new TeacherException(Type.EMPTY_NAME);
		}
		Integer ptid=null;
		Integer pid=null;
		try {
			pid=Integer.parseInt(pd.getPairID());
		} catch (Exception e) {
			throw new TeacherException(Type.BAD_PAIR_ID);
		}		
		TeacherPair tp = MySqlConnector.getPairById(pid);
		try {
			ptid=Integer.parseInt(pd.getPairTeacherID());
		} catch (Exception e) {
			throw new TeacherException(Type.BAD_ID);
		}
		if(ptid>0) {
			MySqlConnector.getTeacher(ptid);
			MySqlConnector.addPairToTeacher(tp.getTeacherID(), pairName);
		} else {
			MySqlConnector.addNewUnknownPairs(new TeacherPair[] {
					new TeacherPair(pairName)
			});
		}
		MySqlConnector.removePairById(pd.getPairID());
	}
	@RequestMapping(value = "/teachers", method = RequestMethod.POST)
	public String teachersController(@RequestParam(value = "type", required = true, defaultValue = "none") String type,
			@RequestBody(required = false) String jsonRequest) {
		TeachersData td = null;
		try {
			//System.out.println(jsonRequest);
			checkIsJson(jsonRequest);
			WebResponse response;
			switch(type) {
				case("add"):
					td = JsonParser.fromJson(jsonRequest, TeachersData.class);
					checkPassword(td.getPassword());
					Teacher addedTeacher = MySqlConnector.addTeacher(td.getTeacherName());
					response = WebResponse.create("added");
					response.setDesc(addedTeacher.getId()+","+addedTeacher.getFullName());
					updateTeachersHash(true);
					Logger.printInfo("Teacher | Success | Add teacher by \nIP: "+(td!=null?td.getIp():"null")+"\nName: "+addedTeacher.getFullName());
					return response.toJson();
				case("update"):
					td = JsonParser.fromJson(jsonRequest, TeachersData.class);
					checkPassword(td.getPassword());
					Teacher updatedTeacher = MySqlConnector.updateTeacher(td.getId(),td.getTeacherName());
					response = WebResponse.create("updated");
					response.setDesc(updatedTeacher.getId()+","+updatedTeacher.getFullName());
					updateTeachersHash(true);
					Logger.printInfo("Teacher | Success | Update teacher by \nIP: "+(td!=null?td.getIp():"null")+"\nName: "+updatedTeacher.getFullName());
					return response.toJson();
				case("delete"):
					td = JsonParser.fromJson(jsonRequest, TeachersData.class);
					checkPassword(td.getPassword());
					if(MySqlConnector.deleteTeacher(td.getId())) {					
						updateTeachersHash(true);
						Logger.printInfo("Teacher | Success | Delete teacher by \nIP: "+(td!=null?td.getIp():"null"));
						updatePairsHash(true);
						return WebResponse.create("ok").toJson();
					} else {
						Logger.printInfo("Teacher | Error | Delete teacher by \nIP: "+(td!=null?td.getIp():"null"));
						return WebResponse.createError("badid").toJson();
					}
				default:
					throw new TeacherException(Type.UNKNOWN_ACTION);
			}
		} catch (TeacherException te) {
			Logger.printInfo("Teacher | Error | "+te.getType().toString()+"\nIP: "+(td!=null?td.getIp():"null"));
			System.out.println("Exception gotcha! : "+te.getType().toString());
			switch(te.getType().toString()) {
				case("BAD_PASSWORD"):
					try {
						Thread.sleep(3500);
					} catch (InterruptedException ignored) {
					}
					return WebResponse.createError("badpass").toJson();
				case("ALREADY_ADDED"):
					return WebResponse.createError("already").toJson();
				case("BAD_ID"):
					return WebResponse.createError("badid").toJson(); 
				case("EMPTY_NAME"):
					return WebResponse.createError("empty").toJson();
				default:
					return WebResponse.createError(null).toJson();
			}
		} catch(Exception e) {
			return WebResponse.createError(null).toJson();
		}
	}
	private void checkPassword(String password) throws TeacherException {
		if(!(DigestUtils.md5Hex(PASSWORD).equalsIgnoreCase(password))) {
			throw new TeacherException(Type.BAD_PASSWORD);
		}
	}
	private void checkIsJson(String jsonRequest) throws IllegalAccessException {
		if(!(JsonParser.isJson(jsonRequest))) {
			throw new IllegalAccessException("Not json!");
		}
	}
	@RequestMapping(value = "/tools", method = RequestMethod.POST)
	public String beautifyGroupName(@RequestParam(value = "type", required = true, defaultValue = "none") String type,
			@RequestBody(required = false) String jsonRequest) throws InterruptedException {
		//System.out.println(jsonRequest);
		if(jsonRequest!=null) {
			if(JsonParser.isJson(jsonRequest)) {
				if(type.equals("gbgn")) {
					GroupName group = JsonParser.fromJson(jsonRequest, GroupName.class);
					//System.out.println(group.getGroupName());
					return TimetableClient.removeTrash(TimetableClient.toRus(group.getGroupName()));
				} else {			
					return "{\"error\":\"true\"}";
				}
			}
		}
		{
			Logger.printInfo("WebFormAuth: Not Json");
			return "{\"error\":\"true\"}";
		}
	}
	@RequestMapping(value = "/mainshelude", method = RequestMethod.POST)
	synchronized public String mainsheludeSetController(@RequestParam(value = "type", required = true, defaultValue = "none") String type,
			@RequestBody(required = false) String jsonRequest) throws InterruptedException {
			if(JsonParser.isJson(jsonRequest)) {
				if(type.equals("updatems")) {
					MainsheludeData md = JsonParser.fromJson(jsonRequest, MainsheludeData.class);
					if(DigestUtils.md5Hex(PASSWORD).equalsIgnoreCase(md.getPassword())) {
						Logger.printInfo("WebFormAuth: Success\nIP: "+md.getIp());
					} else {
						Logger.printInfo("WebFormAuth: Bad Password\nIP: "+md.getIp());
						Thread.sleep(5000);
						return "{\"error\":\"true\",\"desc\":\"badpasswd\"}";
					}
					try {
						md.fixData();
						MySqlConnector.updateMainShelude(md.getGroups());
						groupsAllHash=null;
						mains=null;
						updateHash();
						return "{\"error\":\"false\"}";
					} catch(TimetableException te) {
						te.printStackTrace();
						return "{\"error\":\"true\"}";
					}
				} else if(type.equals("updatebs")) {
					MainsheludeData md = JsonParser.fromJson(jsonRequest, MainsheludeData.class);
					if(DigestUtils.md5Hex(PASSWORD).equalsIgnoreCase(md.getPassword())) {
						Logger.printInfo("WebFormAuth: Success\nIP: "+md.getIp());
					} else {
						Logger.printInfo("WebFormAuth: Bad Password\nIP: "+md.getIp());
						Thread.sleep(5000);
						return "{\"error\":\"true\",\"desc\":\"badpasswd\"}";
					}
					try {
						MySqlConnector.updateBotSettings(md.getBotSettings());
						return "{\"error\":\"false\"}";
					} catch(TimetableException te) {
						te.printStackTrace();
						return "{\"error\":\"true\"}";
					}
				} else {			
					return "{\"error\":\"true\"}";
				}
			} else {
				Logger.printInfo("WebFormAuth: Not Json");
				return "{\"error\":\"true\"}";
			}
		}
}
