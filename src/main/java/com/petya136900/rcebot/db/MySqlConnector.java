package com.petya136900.rcebot.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import com.petya136900.rcebot.handlers.TeacherHandler;
import com.petya136900.rcebot.lifecycle.HChanManga;
import com.petya136900.rcebot.pdfparser.TimetablePDF;
import com.petya136900.rcebot.pdfparser.TimetablePDFPage;
import com.petya136900.rcebot.pdfparser.TimetablePDFQuadro;
import com.petya136900.rcebot.rce.teachers.Teacher;
import com.petya136900.rcebot.rce.teachers.TeacherException;
import com.petya136900.rcebot.rce.teachers.TeacherPair;
import com.petya136900.rcebot.rce.teachers.TeacherException.Type;
import com.petya136900.rcebot.rce.timetable.BlockPair;
import com.petya136900.rcebot.rce.timetable.FileType;
import com.petya136900.rcebot.rce.timetable.GroupsHistoryObject;
import com.petya136900.rcebot.rce.timetable.MainShelude;
import com.petya136900.rcebot.rce.timetable.PeerSettings;
import com.petya136900.rcebot.rce.timetable.SQLData;
import com.petya136900.rcebot.rce.timetable.Timetable;
import com.petya136900.rcebot.rce.timetable.TimetableBlock;
import com.petya136900.rcebot.rce.timetable.TimetableClient;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.rce.timetable.TimetableException.ExceptionCode;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.Properties;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.tools.Foo;
import com.petya136900.rcebot.vk.VK;

public class MySqlConnector {
	public static String toStringStatic() {
		try { connectToDB(); } catch (Exception ignored) {}
		StringBuilder sb = new StringBuilder()
				.append("DB_URI: " + DB_URI + "\n")
				.append("DB_PORT: " + DB_PORT + "\n")
				.append("DB_USERNAME: " + DB_USERNAME + "\n")
				.append("DB_PASSWORD: " + DB_PASSWORD + "\n")
				.append("DB_NAME: " + DB_NAME + "\n")
				.append("ENCODING: " + ENCODING + "\n")
				.append("TIMEZONE: " + TIMEZONE + "\n")
				.append("DB_ENGINE: " + DB_ENGINE + "\n");
		sb.append("\nConnection opened: ");
		try { sb.append(conn.isClosed()); } catch (Exception e) {
			sb.append("Error: "+e.getLocalizedMessage());
		}
		sb.append("\nDB Exist: "+dbExist);
		return sb.toString();
	}
	private static final String     DB_URI       = Properties.getProperty("DB_URI", "localhost");
	private static final Integer    DB_PORT      = Properties.getProperty("DB_PORT", 3306);
	private static final String     DB_USERNAME  = Properties.getProperty("DB_USERNAME","root");
	private static final String     DB_PASSWORD  = Properties.getProperty("DB_PASSWORD", "136900");
	private static final String     DB_NAME      = Properties.getProperty("DB_NAME", "rce_bot");
	private static final String     ENCODING     = Properties.getProperty("DB_ENCODING", "UTF-8");
	private static final String     TIMEZONE     = Properties.getProperty("DB_TIMEZONE", "Europe/Moscow");
	private static final String     DB_ENGINE    = Properties.getProperty("DB_ENGINE", "InnoDB");
	private static       Connection conn;
	private static       Boolean    dbExist      = false;
	//private static       Integer openStatementsCounter = 0;
	//private static       Integer openStatementsLimit   = 250;
	private static       Boolean reconnectPending = false;
	private static final Semaphore  locker       = new Semaphore(1);
	public  static void      hello(String string,Integer delay)                    throws TimetableException {
		checkMySqlServer();
		try {
			ResultSet rs;
			PreparedStatement sql = conn.prepareStatement("SELECT * FROM test WHERE name LIKE '"+string+"'");			
			for(int i=0;i<5;i++) {
				rs=sql.executeQuery();
				sql.close();
				System.out.println(Thread.currentThread().getName()+" | "+
				(rs.next()?rs.getString("name"):"Not Found"));
				try {
					Thread.sleep(delay*i);
				} catch(InterruptedException ie) {
					ie.printStackTrace();
				}
				rs.close();
			}
		} catch(SQLException sqle) {
			disc();
		} //finally {
			//reconnect();
		//}
		System.out.println("Hello, "+string);
	}
	public  static BotSettings    getBotSettings()                                      throws TimetableException {
		checkMySqlServer();
		try(ResultSetRaccoon rs= sqlExecuteQuery(conn,"SELECT (`jsonData`) FROM `"+DB_NAME+"`.`settings`");) {
			if(rs.next()) {
				return JsonParser.fromJson(rs.getString("jsonData"),BotSettings.class);
			} else {
				return null;
			}			
		} catch(SQLException sqle) {
			disc();		
		    class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);
		} //finally {
			//reconnect();
		//}
	}
	public  static String    getGroupNameByPeerID(Integer peerID)                  throws TimetableException {
		checkMySqlServer();
		try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT groupName FROM peer_ids WHERE peer_id LIKE '"+peerID+"'");) {
			if(rs.next()) {
				return rs.getString("groupName");
			} else {
				return null;
			}
		} catch(SQLException sqle) {
			disc();		
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);
		} //finally {
			//reconnect();
		//}
	}
	private static void      disc() {
		if(conn!=null) {
			try {
				conn.close();
			} catch(Exception e) {
				//
			}
		}
		conn=null;
		dbExist=false;
	}
	synchronized private static void reconnect() throws TimetableException {
		if(reconnectPending) {
			System.out.println("Очистка подключенияя");
			disc();
			connectToDB();
			reconnectPending=false;
			locker.release(2);
		}
	}
	synchronized private static void      checkMySqlServer()                                    throws TimetableException {
		if(!dbExist) {
			//System.out.println(Thread.currentThread().getName()+" | "+"Таблица не выбрана");
			synchronized (dbExist) {
				if(conn==null) {
					connectToDB();
				}	
			}
		} else {
			//System.out.println("GOOD");		
		}
	}
	private static void      connectToDB() throws TimetableException {
		//System.out.println(Thread.currentThread().getName()+" | "+"Соединение не установлено");
		try {					
			conn = DriverManager.getConnection("jdbc:mysql://"+DB_URI+":"+DB_PORT+""
					+ "?characterEncoding="+ENCODING+""
							+ "&serverTimezone="+TIMEZONE
							+ "&autoReconnect=true",
							DB_USERNAME,
							DB_PASSWORD);
			//System.out.println(Thread.currentThread().getName()+" | "+"Соединение установлено");
			checkDB();
		} catch (SQLException sqle) {
			// Can't connect to MySql
			class MN {}; throw new TimetableException(ExceptionCode.SQL_CONNECT_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);
		}			
	}
	private static void      checkDB()                                             throws TimetableException {
		try {
			conn.createStatement().executeUpdate("CREATE DATABASE "+DB_NAME+"");
			//System.out.println(Thread.currentThread().getName()+" | "+"Creating DB..");
			connectToTable();
		} catch(SQLException sqle) {
			if(!dbExist) {
				if(sqle.getMessage().contains("database exists")) {
					//System.out.println(Thread.currentThread().getName()+" | "+"DB EXIST");
					connectToTable();
				} else {
					// UNKWN ERROR DURING DB CHECK;		
					disc();							
					class MN {}; throw new TimetableException(ExceptionCode.SQL_ERROR_CREATING_TABLE,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle,DB_NAME);
				}
			}
		}
	}
	private static void      connectToTable()                                      throws TimetableException {
		Integer step=0;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://"+DB_URI+":"+DB_PORT+"/"+DB_NAME+"?characterEncoding="+ENCODING+"&serverTimezone="+TIMEZONE,DB_USERNAME,DB_PASSWORD);
			step=1;
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SHOW TABLES LIKE 'settings'");) {
				if(!rs.next()) {
					step=2;
					sqlExecuteUpdate(conn,"CREATE TABLE `"+DB_NAME+"`.`settings` ( "
								+ "`jsonData` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL "
							+ ") ENGINE = "+DB_ENGINE+";");
					step=3;
					//System.out.println("Инсертим "+JsonParser.toJson(new BotSettings()));
					sqlExecuteUpdate(conn,"INSERT INTO `"+DB_NAME+"`.`settings` (`jsonData`) VALUES ('"+JsonParser.toJson(new BotSettings())+"')");
				} else {
					//System.out.println("ALREADY INSERTED");
				}
			}
			step=4;
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SHOW TABLES LIKE 'peer_ids'");) {
				if(!rs.next()) {
					step=5;
					sqlExecuteUpdate(conn,"CREATE TABLE `"+DB_NAME+"`.`peer_ids` ( "
								+ "`peer_id` INT(32) NOT NULL , "
								+ "`groupName` VARCHAR(32) NOT NULL , "
								+ "`notifications` INT(2) NOT NULL DEFAULT '0', "
								+ "`lastPairs` VARCHAR(256) NOT NULL DEFAULT 'none', "
								+ "`notifHour` INT(2) NOT NULL DEFAULT '10' , "
								+ "`workMode` INT(2) NOT NULL DEFAULT '2' "
							+ ") ENGINE = "+DB_ENGINE+";");
				} else {
					//System.out.println("ALREADY CREATED");
				}	
			}
			step=6;
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SHOW TABLES LIKE 'file_types'");) {
				if(!rs.next()) {
					step=7;
					sqlExecuteUpdate(conn,"CREATE TABLE `"+DB_NAME+"`.`file_types` ( "
								+ "`day` VARCHAR(8) NOT NULL , "
								+ "`ver` VARCHAR(6) NOT NULL , "
								+ "`type` TINYINT(2) NOT NULL , "
								+ "`parsed` TINYINT NOT NULL "
							+ ") ENGINE = "+DB_ENGINE+";");
				} else {
					//System.out.println("ALREADY CREATED");
				}	
			}
			step=8;
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SHOW TABLES LIKE 'timetables'");) {
				if(!rs.next()) {
					step=9;
					sqlExecuteUpdate(conn,"CREATE TABLE `"+DB_NAME+"`.`timetables` ( "
								+ "`day` DATE NOT NULL , "
								+ "`ver` VARCHAR(6) NOT NULL , "
								+ "`groupName` VARCHAR(32) NOT NULL , "
								+ "`jsonData` TEXT NOT NULL "
							+ ") ENGINE = "+DB_ENGINE+";");
				} else {
					//System.out.println("ALREADY CREATED");
				}	
			}
			step=10;
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SHOW TABLES LIKE 'day_messages'");) {
				if(!rs.next()) {
					step=11;
					sqlExecuteUpdate(conn,"CREATE TABLE `"+DB_NAME+"`.`day_messages` ( "
								+ "`day` VARCHAR(8) NOT NULL , "
								+ "`ver` VARCHAR(6) NOT NULL , "
								+ "`text` TEXT NOT NULL "
							+ ") ENGINE = "+DB_ENGINE+";");
				} else {
					//System.out.println("ALREADY CREATED");
				}	
			}
			step=12;
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SHOW TABLES LIKE 'calls'");) {
				if(!rs.next()) {
					step=13;
					sqlExecuteUpdate(conn,"CREATE TABLE `"+DB_NAME+"`.`calls` ( "
								+ "`day` VARCHAR(8) NOT NULL , "
								+ "`ver` VARCHAR(6) NOT NULL , "
								+ "`text` TEXT NOT NULL "
							+ ") ENGINE = "+DB_ENGINE+";");
				} else {
					//System.out.println("ALREADY CREATED");
				}		
			}
			step=14;
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SHOW TABLES LIKE 'mainshelude'");) {
				if(!rs.next()) {
					step=15;
					sqlExecuteUpdate(conn,"CREATE TABLE `"+DB_NAME+"`.`mainshelude` ( "
								+ "`groupName` VARCHAR(32) NOT NULL , "
								+ "`weekDay` VARCHAR(10) NOT NULL , "
								+ "`jsonData` TEXT NOT NULL "
							+ ") ENGINE = "+DB_ENGINE+";");
				} else {
					//System.out.println("ALREADY CREATED");
				}				
			}
			step=16;
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SHOW TABLES LIKE 'hchan_users'");) {
				if(!rs.next()) {
					step=17;
					sqlExecuteUpdate(conn,"CREATE TABLE `"+DB_NAME+"`.`hchan_users` ( "
								+ "`usersID` VARCHAR(16) NOT NULL , "
								+ "`notifyEnable` BOOLEAN NOT NULL DEFAULT FALSE, "
								+ "`notifyTime` INT(2) NOT NULL DEFAULT '0', "
								+ "`lastSendedHashes` TEXT NOT NULL, "
								+ "`lastSendedHash` VARCHAR(32) NOT NULL DEFAULT 'null', "
								+ "`lastRequestHash` VARCHAR(32) NOT NULL DEFAULT 'null', "
								+ "`lastRequest` MEDIUMTEXT NOT NULL, "
								+ "`jsonData` TEXT NOT NULL  "
							+ ") ENGINE = "+DB_ENGINE+";");
				} else {
					//System.out.println("ALREADY CREATED");
				}	
			}
			step=18;
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SHOW TABLES LIKE 'teachers'");) {
				if(!rs.next()) {
					step=19;
					sqlExecuteUpdate(conn,"CREATE TABLE `"+DB_NAME+"`.`teachers` ( "
								+ " `id` INT NOT NULL AUTO_INCREMENT ,"
								+ " `fullName` VARCHAR(300) NOT NULL ,"
								+ " PRIMARY KEY (`id`)"
							+ ") ENGINE = "+DB_ENGINE+";");
				} else {
					//System.out.println("ALREADY CREATED");
				}				
			}
			step=20;
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SHOW TABLES LIKE 'teachers_pairs'");) {
				if(!rs.next()) {
					step=21;
					sqlExecuteUpdate(conn,"CREATE TABLE `"+DB_NAME+"`.`teachers_pairs` ( "
								+ " `id` INT NOT NULL AUTO_INCREMENT ,"
								+ " `type` INT(1) NOT NULL DEFAULT '0' ,"
								+ " `name` VARCHAR(200) NOT NULL ,"
								+ " `teacherID` INT NULL ,"
								+ " PRIMARY KEY (`id`)"
							+ ") ENGINE = "+DB_ENGINE+";");
				} else {
					//System.out.println("ALREADY CREATED");
				}				
			}			
			step=21;
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SHOW TABLES LIKE 'keyboards'");) {
				if(!rs.next()) {
					step=22;
					sqlExecuteUpdate(conn,"CREATE TABLE `"+DB_NAME+"`.`keyboards` ( "
								+ "`peer_id` INT(32) NOT NULL , "
								+ "`replaced` BOOLEAN NOT NULL DEFAULT FALSE"
							+ ") ENGINE = "+DB_ENGINE+";");
				} else {
					//System.out.println("ALREADY CREATED");
				}				
			}	
			step=23;
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SHOW TABLES LIKE 'group_history'");) {
				if(!rs.next()) {
					step=24;
					sqlExecuteUpdate(conn,"CREATE TABLE `"+DB_NAME+"`.`group_history` ( "
								+ "`keyName` VARCHAR(64) NOT NULL , "
								+ "`groupName` VARCHAR(32) NOT NULL "
							+ ") ENGINE = "+DB_ENGINE+";");
				} else {
					//System.out.println("ALREADY CREATED");
				}				
			}	
			step=25;
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SHOW TABLES LIKE 'cab_history'");) {
				if(!rs.next()) {
					step=26;
					sqlExecuteUpdate(conn,"CREATE TABLE `"+DB_NAME+"`.`cab_history` ( "
								+ "`keyName` VARCHAR(64) NOT NULL , "
								+ "`groupName` VARCHAR(32) NOT NULL "
							+ ") ENGINE = "+DB_ENGINE+";");
				} else {
					//System.out.println("ALREADY CREATED");
				}				
			}	
			dbExist=true;			
			//System.out.println(Thread.currentThread().getName()+" | "+"Подключен к таблице-БД: "+DB_NAME);
		} catch(SQLException sqle) {
			//System.out.println(Thread.currentThread().getName()+" | "+"Не удалось подключиться к таблице: "+DB_NAME);
			sqle.printStackTrace();
			disc();					
			class MN {}; throw new TimetableException(ExceptionCode.SQL_CHECK_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle,step+"");
		}
	}
	public  static void      updateBotSettings(BotSettings botSettings)            throws TimetableException {
		checkMySqlServer();
		try {
			sqlExecuteUpdate(conn,"UPDATE `"+DB_NAME+"`.`settings` SET `jsonData` = '"+JsonParser.toJson(botSettings)+"'");
		} catch (SQLException sqle) {
			disc();		
		    class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);		    
		} catch (Exception e) {
			reconnect();
		}
	}
	private static ResultSetRaccoon sqlExecuteQuery(Connection conn, String sqlString)    throws SQLException {
		//try(
		//System.out.println("Запрос: "+sqlString);
		PreparedStatement sql = conn.prepareStatement(sqlString);
		ResultSet rs = sql.executeQuery();
			//	) {
		return new ResultSetRaccoon(rs);
		//}
	}	
	private static void       sqlExecuteUpdate(Connection conn, String sqlString)   throws SQLException {
		//System.out.println("Запрос обновления: "+sqlString);
		try(		
				PreparedStatement sql = conn.prepareStatement(sqlString);
				) {		
				sql.executeUpdate();
		}		
	}
	public  static void      dropDB() throws TimetableException {
		checkMySqlServer();
		try {
			sqlExecuteUpdate(conn,"DROP DATABASE `"+DB_NAME+"`");
		} catch (SQLException sqle) {
			disc();		
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);
		} //finally {
			//reconnect();
		//}
	}
	public  static void      setGroupNameByPeerID(Integer peerID,String groupName) throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT groupName FROM peer_ids WHERE peer_id ='"+peerID+"'");) {
				if(rs.next()) {
					sqlExecuteUpdate(conn,"UPDATE `"+DB_NAME+"`.`peer_ids` SET `groupName` = '"+groupName+"' WHERE peer_id='"+peerID+"'");
				} else {
					sqlExecuteUpdate(conn,"INSERT INTO `"+DB_NAME+"`.`peer_ids` (`peer_id`, `groupName`, `notifications`) VALUES ('"+peerID+"','"+groupName.trim()+"','0')");
				}
			}
			addToGroupHistory(peerID,groupName);
		} catch(SQLException sqle) {
			disc();		
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);
		} //finally {
			//reconnect();
		//}
	}
	private static void addToGroupHistory(Integer peerID, String groupName) throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM group_history WHERE keyName ='"+peerID+"_"+peerID+"' AND groupName = '"+groupName+"'");) {
				if(rs.next()) {
					//
				} else {
					sqlExecuteUpdate(conn,"INSERT INTO `"+DB_NAME+"`.`group_history` (`keyName`, `groupName`) VALUES ('"+peerID+"_"+peerID+"','"+groupName+"')");
				}
			}
		} catch(SQLException sqle) {
			disc();		
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);
		}
	}
	public  static SQLData[] getAllTimetables() throws TimetableException {
		return getTimetableByExpr("SELECT * FROM `timetables`");
	}
	private static SQLData[] getTimetableByExpr(String sqlExpr)                    throws TimetableException {
		checkMySqlServer();
		try {
			ResultSetRaccoon rs = sqlExecuteQuery(conn, sqlExpr);
			return ParseForSQLData(rs);
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);
		} //finally {
			//reconnect();
		//}
	}
	public  static SQLData[] getTimetablesByDay(String day)                        throws TimetableException {
		return getTimetableByExpr("SELECT * FROM `timetables` WHERE day LIKE '"+SQLData.dayToSQLDate(day)+"'");
	}
	private static SQLData[] ParseForSQLData(ResultSetRaccoon rs)                         throws TimetableException, SQLException {
		ArrayList<SQLData> sqlData = new ArrayList<SQLData>();
		while(rs.next()) {
			sqlData.add(new SQLData(rs.getRs()));
		}
		rs.close();
		return sqlData.toArray(new SQLData[sqlData.size()]);
	}
	public static String getLastVer(String date) throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn, "SELECT * FROM `file_types` "
					+ "WHERE type='1' "
					+ "AND day='"+date+"'"
					+ "AND parsed='1' "
					+ "ORDER BY ver DESC");) {
				if(rs.next()) {
					return rs.getString("ver");
				} else {
					return null;
				}
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);
		} //finally {
			//reconnect();
		//}
	}
	public static FileType[] getFileTypes(String date) throws TimetableException {
		checkMySqlServer();
		ArrayList<FileType> types = new ArrayList<FileType>();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM `file_types` WHERE day LIKE '"+date+"'");) {
				while(rs.next()) {
					types.add(new FileType(rs.getString("day"),rs.getString("ver"),rs.getInt("type"),rs.getInt("parsed")));
				}
			}
			return types.toArray(new FileType[types.size()]);
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);
		} //finally {
			//reconnect();
		//}
	}
	public static void saveTimetable(TimetablePDF t) throws TimetableException {
		checkMySqlServer();
		MySqlConnector.prepareParse(t.getDay(),t.getVer(),t.getType(),true);
		TeacherPair[] oldTeacherPairs = MySqlConnector.getTeachersPairs();
		ArrayList<TeacherPair> newTeacherPairs = new ArrayList<TeacherPair>();
		try {
			for(TimetablePDFPage page : t.getPages()) {
				if(page.isCalls()) {
					if(page.getOtherStringData()!=null) {
						if(page.getOtherStringData().trim().length()>0) {
							// Save to calls
							updateCalls(t.getDay(),page.getOtherStringData());
						}
					}
					continue;
				} else if(!page.getIsTimetable()) {
					if(page.getOtherStringData()!=null) {
						if(page.getOtherStringData().trim().length()>0) {
							// Save to day_messages
							updateDayMessage(t.getDay(),page.getOtherStringData());
						}
					}					
					continue;
				} else if(page.getIsTimetable()) {
					for(TimetablePDFQuadro part : page.getParts()) {
						for(TimetableBlock block : part.getBlocks()) {
							Timetable tt = new Timetable(block.getPairs().toArray(new BlockPair[block.getPairs().size()]), 
														block.getTeachers(), 
														part.getIsPractic());
							// FOR Teacher command work
							for(BlockPair pair : tt.getPairs()) {
								Boolean stopFlag=false;
								for(TeacherPair newPair : newTeacherPairs) {
									if(stopFlag) {
										break;
									}
									if(pair.getPairName().toLowerCase().trim().equalsIgnoreCase(newPair.getName().trim())) {
										stopFlag=true;
										break;
									}
								}
								for(TeacherPair oldPair : oldTeacherPairs) {
									if(stopFlag) {
										break;
									}
									if(pair.getPairName().toLowerCase().trim().equalsIgnoreCase(oldPair.getName().trim())) {
										stopFlag=true;
										break;
									}
								}					
								if(!stopFlag) {
									newTeacherPairs.add(new TeacherPair(pair.getPairName()));
								}
							}
							// End FOR
							//System.out.println("Блок пар: "+JsonParser.toJson(tt));
							try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM timetables"
										+ " WHERE day = '"+SQLData.dayToSQLDate(t.getDay())+"' "
										+ " AND ver ='"+t.getVer()+"'"
										+ " AND groupName = '"+block.getGroupName().trim()+"'");) {
								if(rs.next()) {
									//System.out.println("Обновляем данные");
									sqlExecuteUpdate(conn,"UPDATE `"+DB_NAME+"`.`timetables` "
											+ "SET `jsonData` = '"+JsonParser.toJson(tt)+"'"
											+ "WHERE day='"+SQLData.dayToSQLDate(t.getDay())+"' AND ver='"+t.getVer()+"' "
											+ " AND groupName = '"+block.getGroupName().trim()+"'");
								} else {
									//System.out.println("Добавляем данные");
									sqlExecuteUpdate(conn,"INSERT INTO `"+DB_NAME+"`.`timetables` "
											+ "(`day`, `ver`, `groupName`, `jsonData`) "
											+ "VALUES "
											+ "('"
													+SQLData.dayToSQLDate(t.getDay())+"', "
													+ "'"+t.getVer()+"', "
													+ "'"+block.getGroupName().trim()+"', "
													+ "'"+JsonParser.toJson(tt)+"'"
											+ ")");
								}	
							}
						}
					}
				}
			}
			// For Teacher command work
			try {
				MySqlConnector.addNewUnknownPairs(newTeacherPairs.toArray(new TeacherPair[newTeacherPairs.size()]));
			} catch (Exception e) {
				//
			}
			//
		} catch(SQLException sqle) {
			VK.sendMessage(VK.getAdminID(), sqle.getMessage());
			for(StackTraceElement stElement :  sqle.getStackTrace()) {
				VK.sendMessage(VK.getAdminID(), stElement.getClassName()+":"+stElement.getMethodName()+":"+stElement.getLineNumber());
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					throw new RuntimeException(e1);
				}
			}
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		} //finally {
			//reconnect();
		//}
	}
	private static void updateDayMessage(String day, String otherStringData) throws TimetableException {
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM day_messages WHERE day = '"+day+"'");) {
				if(rs.next()) {
					sqlExecuteUpdate(conn,"UPDATE `"+DB_NAME+"`.`day_messages` SET `text` = '"+otherStringData+"' WHERE day = '"+day+"'");
				} else {
					sqlExecuteUpdate(conn,"INSERT INTO `"+DB_NAME+"`.`day_messages` (`day`,`ver`,`text`) VALUES ('"+day+"','','"+otherStringData+"')");
				}
			}
		} catch(SQLException sqle) {
			//sqle.printStackTrace();
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	private static void updateCalls(String day, String otherStringData) throws TimetableException {
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM calls WHERE day = '"+day+"'");) {
				if(rs.next()) {
					sqlExecuteUpdate(conn,"UPDATE `"+DB_NAME+"`.`calls` SET `text` = '"+otherStringData+"' WHERE day = '"+day+"'");
				} else {
					sqlExecuteUpdate(conn,"INSERT INTO `"+DB_NAME+"`.`calls` (`day`,`ver`,`text`) VALUES ('"+day+"','','"+otherStringData+"')");
				}
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static void prepareParse(String date, String ver, String type, Boolean parsed) throws TimetableException {
		int i=0;
		if(parsed) {
			i=1;
		}
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM file_types WHERE day = '"+date+"' AND ver = '"+ver+"'");) {
				if(rs.next()) {
					sqlExecuteUpdate(conn,"UPDATE `"+DB_NAME+"`.`file_types` SET `type` = '"+type+"',`parsed` = '"+i+"' WHERE day='"+date+"' and ver='"+ver+"'");
				} else {
					sqlExecuteUpdate(conn,"INSERT INTO `"+DB_NAME+"`.`file_types` (`day`, `ver`, `type`, `parsed`) VALUES ('"+date+"','"+ver+"','"+type+"', '"+i+"')");
				}
			}
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		} //finally {
			//reconnect();
		//}
	}
	public static Timetable getTimetableByDayAndGroup(String date, String groupName) throws TimetableException {
		checkMySqlServer();
		try {
			String ver = getLastVer(date);
			if(ver!=null) {
				try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM timetables"
						+ " WHERE day = '"+SQLData.dayToSQLDate(date)+"' "
						+ " AND ver ='"+ver+"'"
						+ " AND groupName = '"+groupName.trim()+"'");) {
					if(rs.next()) {
						//System.out.println("Ответ: "+rs.getString("jsonData"));
						return JsonParser.fromJson(rs.getString("jsonData"),Timetable.class);
					} else {
						return null;
					}
				}
			} else {
				return null;
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		} //finally {
			//reconnect();
		//}	
	}
	public static MainShelude[] getMainShelude() throws TimetableException {
		checkMySqlServer();
		ArrayList<MainShelude> mains = new ArrayList<MainShelude>();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM mainshelude");) {
				while(rs.next()) {
					mains.add(new MainShelude(
							rs.getString("groupName"), 
							rs.getString("weekDay"), 
							JsonParser.fromJson(rs.getString("jsonData"), BlockPair[].class)));
				}
			}
			return mains.toArray(new MainShelude[mains.size()]);
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		} //finally {
			//reconnect();
		//}
	}
	public static void updateMainShelude(MainShelude[] groups) throws TimetableException {
		checkMySqlServer();
		StringBuilder sb = new StringBuilder("");
		try {
			sqlExecuteUpdate(conn,"TRUNCATE TABLE mainshelude");
			if(groups.length>0) {
				Boolean first=true;
				for(MainShelude group : groups) {
					if(first) {
						first=false;
					} else {
						sb.append(",");
					}
					sb.append("('"+group.getGroupName()+"','"+
									group.getWeekDay()+"','"+
									JsonParser.toJson(group.getPairs())+"')"
							);
				}
				sqlExecuteUpdate(conn,"INSERT INTO `"+DB_NAME+"`.`mainshelude` (`groupName`, `weekDay`, `jsonData`) "
						+ "VALUES "+ sb);
			}
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		} //finally {
			//reconnect();
		//}
	}
	public static BlockPair[] getMainShelude(String groupName, String dayOfWeekEng, Boolean bool) throws TimetableException {
		checkMySqlServer();
		ArrayList<BlockPair> pairs = new ArrayList<BlockPair>();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM mainshelude "
					+ "WHERE groupName = '"+groupName+"' "
					+ "AND weekDay='"+dayOfWeekEng+"'");) {
				while(rs.next()) {
					MainShelude ms = new MainShelude(
							rs.getString("groupName"), 
							rs.getString("weekDay"), 
							JsonParser.fromJson(rs.getString("jsonData"), BlockPair[].class));
					ms.fixWeekType(bool);
					pairs.addAll(Arrays.asList(ms.getPairs()));
				}
			}
			return pairs.toArray(new BlockPair[pairs.size()]);
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		} //finally {
			//reconnect();
		//}
	}
	public static String getDayMessage(String date) throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM day_messages "
					+ "WHERE day = '"+date+"' ");) {
				if(rs.next()) {
					if(rs.getString("text").trim().length()>0) {
						return rs.getString("text").trim();
					} 
				}
			}
			return null;
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		} //finally {
			//reconnect();
		//}
	}
	public static String getCallReplace(String date) throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM calls "
					+ "WHERE day = '"+date+"' ");) {
				if(rs.next()) {
					if(rs.getString("text").trim().length()>0) {
						return rs.getString("text").trim();
					} 
				}
			}
			return null;
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		} //finally {
			//reconnect();
		//}
	}
	public static PeerSettings getPeerSettings(Integer peer_id) throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM peer_ids "
					+ "WHERE peer_id = '"+peer_id+"' ");) {
				if(rs.next()) {
					return new PeerSettings(
								rs.getInt("peer_id"),
								rs.getString("groupName"),
								rs.getInt("notifications"),
								rs.getString("lastPairs"),
								rs.getInt("notifHour"),
								rs.getInt("workMode")
							);
				}
			}
			return null;
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		} //finally {
			//reconnect();
		//}
	}
	public static void updatePeerSettings(PeerSettings ps) throws TimetableException {
		checkMySqlServer();
		try {
			sqlExecuteUpdate(conn,"UPDATE `"+DB_NAME+"`.`peer_ids` "
					+ "SET "
					+ "`notifications` = '"+ps.getNotifications()+"',"
					+ "`notifHour` = '"+ps.getNotifHour()+"',"
					+ "`workMode` = '"+ps.getWorkMode()+"'"
					+ " WHERE "
					+ "peer_id='"+ps.getPeer_id()+"'");			
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		} //finally {
			//reconnect();
		//}
	}
	public static PeerSettings[] getPeersWithNotify() throws TimetableException {
		checkMySqlServer();
		ArrayList<PeerSettings> peers= new ArrayList<PeerSettings>();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM peer_ids "
					+ "WHERE notifications = '1'");) {
				while(rs.next()) {
					peers.add(new PeerSettings(
								rs.getInt("peer_id"),
								rs.getString("groupName"),
								rs.getInt("notifications"),
								rs.getString("lastPairs"),
								rs.getInt("notifHour"),
								rs.getInt("workMode")
							));
				}
			}
			return peers.toArray(new PeerSettings[peers.size()]);
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		} //finally {
			//reconnect();
		//}
	}
	public static void updateLastPairs(Integer peerID, String md5String) throws TimetableException {
		checkMySqlServer();
		try {
			sqlExecuteUpdate(conn,"UPDATE `"+DB_NAME+"`.`peer_ids` "
					+ "SET "
					+ "`lastPairs` = '"+md5String+"'"
					+ " WHERE "
					+ "peer_id='"+peerID+"'");			
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		} //finally {
			//reconnect();
		//}
	}
	public static MainShelude[] getMainsTimetables(String dayOfWeekEng, Boolean weekType, String cabinet, String ... groupsWithReplace) throws TimetableException {
		if(groupsWithReplace==null) {
			groupsWithReplace = new String[0];
		}
		//System.out.println("Групп с заменами: "+groupsWithReplace.length);
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM mainshelude "
					+ "WHERE weekDay = '"+dayOfWeekEng+"' AND jsonData LIKE '%"+cabinet+"%'");) {
				ArrayList<MainShelude> mains = new ArrayList<MainShelude>();
				//System.out.println("Array initialized..");
				while(rs.next()) {
					//System.out.println("Результат из бд");
					if(inReplace(groupsWithReplace,rs.getString(1))) {
						//System.out.println(rs.getString(1)+" в замене!");
						continue;
					}
					BlockPair[] bp = JsonParser.fromJson(rs.getString(3), BlockPair[].class);
					for(BlockPair pair : bp) {
						String cabName = pair.getPairCab();
						String pairName = pair.getPairName();
						if(cabName.contains("/")) {
							int sIndex = cabName.indexOf("/");
							if(weekType) {
								cabName = cabName.substring(0,sIndex);
							} else {
								cabName = cabName.substring(sIndex+1,cabName.length());	
							}
						}
						if(pairName.contains("/")) {
							int sIndex = pairName.indexOf("/");
							if(weekType) {
								pairName = pairName.substring(0,sIndex);
							} else {
								pairName = pairName.substring(sIndex+1,pairName.length());	
							}
						}
						if(RegexpTools.checkRegexp("\\b"+cabinet+"\\b", cabName)) {
							mains.add(new MainShelude(rs.getString(1), rs.getString(2), new BlockPair[] {
									new BlockPair(pair.getPairNum(), pairName, cabName)
							}));
						}
					}
				}
				return mains.toArray(new MainShelude[mains.size()]);
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	private static boolean inReplace(String[] groupsWithReplace, String groupName) {
		String fixedGroupName = TimetableClient.removeTrash((TimetableClient.toRus(groupName.trim()))).toLowerCase().replace("к-","-");
		//System.out.println("Чекаем..");
		for(String replace : groupsWithReplace) {
			String fixedReplaceName = TimetableClient.removeTrash((TimetableClient.toRus(replace.trim()))).toLowerCase().replace("к-","-");
			//System.out.print("CGN: "+fixedGroupName+" - "+fixedReplaceName+" | ");
			if(fixedGroupName.equals(fixedReplaceName)) {
				//System.out.println("true");
				return true;
			}
			//System.out.println("false");
		}
		return false;
	}
	public static MainShelude[] getAllTimetablesByCab(String date, String cabinet) throws TimetableException {
		//System.out.println("Вызван gatbc");
		String lastVer = MySqlConnector.getLastVer(date);
		if(lastVer==null) {
			//System.out.println("null");
			return null;
		}
		date = SQLData.dayToSQLDate(date);
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM timetables "
					+ "WHERE day = '"+date+"' AND ver = '"+lastVer+"' AND jsonData LIKE '%"+cabinet+"%'");) {
				//System.out.println("Запрос: "+"SELECT * FROM timetables "
				//	+ "WHERE day = '"+date+"' AND ver = '"+lastVer+"' AND jsonData LIKE '%"+cabinet+"%'");
				ArrayList<MainShelude> replaces = new ArrayList<MainShelude>();
				while(rs.next()) {
					Timetable t = JsonParser.fromJson(rs.getString(4), Timetable.class);
					for(BlockPair pair : t.getPairs()) {
						//System.out.println("Пара: "+pair.getPairCab());
						if(RegexpTools.checkRegexp("\\b"+cabinet+"\\b", pair.getPairCab())) {
							//System.out.println("Добавляю");
							replaces.add(new MainShelude(rs.getString(3), "", new BlockPair[] {
									pair
							}));
						} else if(RegexpTools.checkRegexp("\\b"+cabinet+"\\b", pair.getPairName())) {
							//System.out.println("Добавляю");
							pair.setPairCab(pair.getPairCab());;//+"(Экзамен?)");
							replaces.add(new MainShelude(rs.getString(3), "", new BlockPair[] {
									pair
							}));
						} else {
							//System.out.println("Не добавляю");
						}
					}
				}
				return replaces.toArray(new MainShelude[replaces.size()]);
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static String[] getGroupsWithReplace(String date) throws TimetableException {
		String lastVer = MySqlConnector.getLastVer(date);
		if(lastVer==null) {
			return null;
		}
		date = SQLData.dayToSQLDate(date);
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM timetables "
					+ "WHERE day = '"+date+"' AND ver = '"+lastVer+"'");) {
				ArrayList<String> groupsWithReplace = new ArrayList<String>();
				while(rs.next()) {
					groupsWithReplace.add(rs.getString(3));
				}
				return groupsWithReplace.toArray(new String[groupsWithReplace.size()]);
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}		
	}
	public static Teacher[] getTeachers(String teacher) throws TimetableException {
		checkMySqlServer();
		String[] teacherNameArray = teacher.trim().split(" ");
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM teachers "
					+ "WHERE fullName LIKE '%"+teacherNameArray[0].trim()+"%'");) {
				ArrayList<Teacher> teachers = new ArrayList<Teacher>();
				while(rs.next()) {
					String teacherFullName = rs.getString(2);
					Boolean another=false;
					for(String teacherName : teacherNameArray) {
						if(teacherFullName.trim().toLowerCase().contains(teacherName.toLowerCase().trim())) {
							//
						} else {
							another=true;
						}
					}
					if(another) {
						continue;
					}
					teachers.add(
								new Teacher(rs.getInt(1), rs.getString(2))
							);
				}
				return teachers.toArray(new Teacher[teachers.size()]);
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static TeacherPair[] getTeachersPairs() throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM teachers_pairs");) {
				ArrayList<TeacherPair> teachersPairs = new ArrayList<TeacherPair>();
				while(rs.next()) {
					teachersPairs.add(
								new TeacherPair(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getInt(4))
							);
				}
				return teachersPairs.toArray(new TeacherPair[teachersPairs.size()]);
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static void testPut() throws TimetableException {
		checkMySqlServer();
		try {
			sqlExecuteUpdate(conn,"INSERT INTO `"+DB_NAME+"`.`teachers_pairs` (`name`) "
					+ "VALUES ('pair1java')");
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}	
	}
	public static void addNewUnknownPairs(TeacherPair[] newPairs) throws TimetableException, TeacherException {
		Boolean canThrowErrors=false;
		if(newPairs!=null) {
			if(newPairs.length==1) {
				canThrowErrors=true;
			}
		}
		checkMySqlServer();
		try {
			for(TeacherPair newPair : newPairs) {
				try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM teachers_pairs WHERE name REGEXP '(^"+newPair.getName().toLowerCase().trim().replaceAll(".", "\\\\.")+"$)'");) {
					//System.out.print(newPair.getName()+" | ");
					Boolean added=false;
					
					while(rs.next()) {
						String tempPairName = rs.getString(3);
						if(tempPairName.trim().equalsIgnoreCase(newPair.getName().trim())) {
							if(canThrowErrors) {
								Integer id=rs.getInt(4);
								String fullName;
								if(id>0) {
									try {
										fullName = MySqlConnector.getTeacher(id).getFullName();
									} catch(Exception e) {
										fullName="{bad_id}";
									}
									TeacherException te = new TeacherException(Type.BUSY_BY);
									te.setTeacher(new Teacher(id,fullName));
									throw te;
								} else {
									throw new TeacherException(Type.ALREADY_UNKNOWN);
								}
							}
							added=true;
							break;
						}
					}
					if(added) {
						//System.out.println("Пара уже добавлена!");
					} else {
						//System.out.println("Пары не было, добавляем");
						sqlExecuteUpdate(conn,"INSERT INTO `"+DB_NAME+"`.`teachers_pairs` (`name`,`teacherID`) "
								+ "VALUES ('"+newPair.getName().trim()+"','0')");
					}
				}
			}
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static Teacher getTeacher(Integer id) throws TimetableException, TeacherException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM teachers WHERE id='"+id+"'");) {
				if(rs.next()) {
					return new Teacher(rs.getInt(1), rs.getString(2));
				} else {
					throw new TeacherException(Type.BAD_ID);
				}
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static TeacherPair[] getTeachersPairs(Integer teacherID) throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM `teachers_pairs` WHERE teacherID='"+teacherID+"'");) {
				ArrayList<TeacherPair> teachersPairs = new ArrayList<TeacherPair>();
				while(rs.next()) {
					teachersPairs.add(
								new TeacherPair(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getInt(4))
							);
				}
				return teachersPairs.toArray(new TeacherPair[teachersPairs.size()]);
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static MainShelude[] getAllTimetablesByPairNames(String date, TeacherPair[] teacherPairs) throws TimetableException {
		if(teacherPairs.length<1) {
			throw new IllegalArgumentException("teachersPairs.length can't be <1!");
		}
		String regexpPairs,regexpPairsForJava=null;
		regexpPairs=creacteRegexp(teacherPairs,"\\\\b");
		regexpPairsForJava=creacteRegexp(teacherPairs,new String[]{"^","$"});
		////////
		//System.out.println("SQL: "+regexpPairs);
		//System.out.println("Java: "+regexpPairsForJava);
		String lastVer = MySqlConnector.getLastVer(date);
		if(lastVer==null) {
			return null;
		}
		date = SQLData.dayToSQLDate(date);
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM timetables "
					+ "WHERE day = '"+date+"' AND ver = '"+lastVer+"' AND jsonData REGEXP '"+regexpPairs+"'");) {
				ArrayList<MainShelude> replaces = new ArrayList<MainShelude>();
				//System.out.println("Запрос: \n"+"SELECT * FROM timetables "
					//+ "WHERE day = '"+date+"' AND ver = '"+lastVer+"' AND jsonData REGEXP '"+regexpPairs+"'");
				while(rs.next()) {
					//System.out.println("Результат..");
					Timetable t = JsonParser.fromJson(rs.getString(4), Timetable.class);
					for(BlockPair pair : t.getPairs()) {
						//System.out.println(pair.getPairName());
						if(RegexpTools.checkRegexp(regexpPairsForJava, pair.getPairName().toLowerCase().trim())) {
							//System.out.println("Добавляем");
							replaces.add(new MainShelude(rs.getString(3), "", new BlockPair[] {
									pair
							}));
						} else {
							//System.out.println("Не добавляем");
						}
					}
				}
				return replaces.toArray(new MainShelude[replaces.size()]);
			}
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}		
	}
	private static String creacteRegexp(TeacherPair[] teacherPairs, String...del) {
		Boolean javaMode=false;
		if(del.length>1) {
			javaMode=true;
		}
		StringBuilder sbrp = new StringBuilder("(");
		Boolean first=true;
		for(TeacherPair teacherPair : teacherPairs) {
			if(!first) {
				sbrp.append("|");
			} else {
				first=false;
			}
			String jsonString=JsonParser.toJson(teacherPair.getName().trim().replaceAll("[^а-яА-Яa-zA-Z0-9]", "(.)?"));
			jsonString=jsonString.replaceAll("(^\\\")|(\\\"$)", "");
			sbrp.append((javaMode?del[0]:del[0])+jsonString.toLowerCase()+(javaMode?del[1]:del[0]));
		}
		sbrp.append(")");
		return "(("+(javaMode?del[0]:del[0])+"|\")+("+ sbrp +")+("+(javaMode?del[1]:del[0])+"|\"))";
	}
	public static MainShelude[] getMainsTimetablesByPairNames(String dayOfWeekEng, Boolean weekType,
			TeacherPair[] teacherPairs, String[] groupsWithReplace) throws TimetableException {
		if(teacherPairs.length<1) {
			throw new IllegalArgumentException("teachersPairs.length can't be <1!");
		}
		String regexpPairs,regexpPairsForJava=null;
		regexpPairs=creacteRegexp(teacherPairs,"\\\\b");
		regexpPairsForJava=creacteRegexp(teacherPairs,"\\b");
		////////
		if(groupsWithReplace==null) {
			groupsWithReplace = new String[0];
		}
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM mainshelude "
					+ "WHERE weekDay = '"+dayOfWeekEng+"' AND jsonData REGEXP '"+regexpPairs+"'");) {
				//System.out.println("Запрос: \n"+"SELECT * FROM mainshelude "
				//		+ "WHERE weekDay = '"+dayOfWeekEng+"' AND jsonData REGEXP '"+regexpPairs+"'");
				ArrayList<MainShelude> mains = new ArrayList<MainShelude>();
				while(rs.next()) {
					//System.out.println("Результат");
					if(inReplace(groupsWithReplace,rs.getString(1))) {
						//System.out.println("Группа есть в замене");
						continue;
					}
					BlockPair[] bp = JsonParser.fromJson(rs.getString(3), BlockPair[].class);
					for(BlockPair pair : bp) {
						String pairName = pair.getPairName();
						String cabName = pair.getPairCab();
						if(pairName.contains("/")) {
							//System.out.println("Есть делитель /");
							int sIndex = pairName.indexOf("/");
							if(weekType) {
								pairName = pairName.substring(0,sIndex);
							} else {
								pairName = pairName.substring(sIndex+1,pairName.length());	
							}
							//System.out.println("После обрезки: "+pairName);
						}
						if(cabName.contains("/")) {
							int sIndex = cabName.indexOf("/");
							if(weekType) {
								cabName = cabName.substring(0,sIndex);
							} else {
								cabName = cabName.substring(sIndex+1,cabName.length());	
							}
						}						
						if(RegexpTools.checkRegexp(regexpPairsForJava, pairName)) {
							mains.add(new MainShelude(rs.getString(1), rs.getString(2), new BlockPair[] {
									new BlockPair(pair.getPairNum(), pairName, cabName)
							}));
						} else {
							//System.out.println("Не добавляем");
						}
					}
				}
				return mains.toArray(new MainShelude[mains.size()]);
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static void testGetMainSSA() throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn, 
					"SELECT * FROM mainshelude WHERE weekDay = 'monday' AND groupName = 'ССА-401'");) {
				while(rs.next()) {
					System.out.println(rs.getString(1));
					System.out.println(rs.getString(2));
					System.out.println(rs.getString(3));
				}
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static Teacher addTeacher(String teacherName) throws TimetableException, TeacherException {
		teacherName = TeacherHandler.removeTrash(teacherName);
		if(teacherName.length()<2) {
			throw new TeacherException(Type.EMPTY_NAME);
		}
		String[] teacherNameArray = teacherName.split(" ");
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn, 
					"SELECT * FROM teachers WHERE fullName LIKE '%"+teacherNameArray[0]+"%'");) {
				while(rs.next()) {
					checkTeacherAlreadyAdded(teacherNameArray,rs.getString(2));
				}
				sqlExecuteUpdate(conn, "INSERT INTO teachers (`fullName`) VALUES ('"+teacherName+"')");
				try(ResultSetRaccoon rs2 = sqlExecuteQuery(conn, 
						"SELECT * FROM teachers WHERE fullName = '"+teacherName+"'");) {
					if(rs2.next()) {
						return new Teacher(rs2.getInt(1),rs2.getString(2));
					} else {
						throw new TeacherException(Type.UNKNOWN_ERROR);
					}
				}
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	private static void checkTeacherAlreadyAdded(String[] teacherNameArray, String createdFullName) throws TeacherException {
		if(teacherNameArray.length<1) {
			throw new IllegalArgumentException("Array length can't be <1");
		}
		int countS = teacherNameArray.length;
		for(String part : teacherNameArray) {
			if(RegexpTools.checkRegexp("(\\b"+part.trim().toLowerCase()+"\\b)", createdFullName.toLowerCase())) {
				countS--;
			}
		}
		if(countS<1) {
			throw new TeacherException(Type.ALREADY_ADDED);
		}
	}
	public static Teacher updateTeacher(String idS, String teacherName) throws TimetableException, TeacherException {
		Integer id=Integer.parseInt(idS);
		teacherName.replace("ё","е");
		teacherName=teacherName.replaceAll("\\s\\s+", " ");
		teacherName = teacherName.replaceAll("([^а-яА-Яa-zA-Z0-9 \\-])", "").trim();
		if(teacherName.length()>300) {
			teacherName=teacherName.substring(0,300).trim();
		}
		if(teacherName.length()<2) {
			throw new TeacherException(Type.EMPTY_NAME);
		}
		String[] teacherNameArray = teacherName.split(" ");
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn, 
					"SELECT * FROM teachers WHERE fullName LIKE '%"+teacherNameArray[0]+"%'");) {
				while(rs.next()) {
					checkTeacherAlreadyAdded(teacherNameArray,rs.getString(2));
				}
				sqlExecuteUpdate(conn, "UPDATE teachers SET fullName = '"+teacherName+"' WHERE id = "+id);
				try(ResultSetRaccoon rs2 = sqlExecuteQuery(conn, 
						"SELECT * FROM teachers WHERE fullName = '"+teacherName+"'");) {
					if(rs2.next()) {
						return new Teacher(rs2.getInt(1),rs2.getString(2));
					} else {
						throw new TeacherException(Type.UNKNOWN_ERROR);
					}
				}
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static Teacher[] getTeachers() throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM teachers ");) {
				ArrayList<Teacher> teachers = new ArrayList<Teacher>();
				while(rs.next()) {
					teachers.add(
								new Teacher(rs.getInt(1), rs.getString(2))
							);
				}
				return teachers.toArray(new Teacher[teachers.size()]);
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static boolean deleteTeacher(Integer id) throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM teachers WHERE id='"+id+"'");) {
				if(rs.next()) {
					try {
						TeacherPair[] oldTeacherPairs = MySqlConnector.getTeachersPairs(id);
						for(TeacherPair oldTeacherPair : oldTeacherPairs) {
							//System.out.println("Пара препода для неизвестных: "+oldTeacherPair.getId()+" | "+oldTeacherPair.getName());
							try {
								sqlExecuteUpdate(conn, "UPDATE teachers_pairs SET type = '0', teacherID = '0' WHERE id = '"+oldTeacherPair.getId()+"'");
								checkUnknownDuplicates(oldTeacherPair.getName());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}	
					} catch(Exception e) {
						//
					}
					sqlExecuteUpdate(conn, "DELETE FROM teachers WHERE id='"+id+"'");
					return true;
				} else {
					return false;
				}
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	private static void checkUnknownDuplicates(String pairName) throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM teachers_pairs WHERE name='"+pairName+"' AND teacherID='0'");) {
				Boolean first=true;
				try(ResultSetRaccoon rs2 = sqlExecuteQuery(conn,"SELECT * FROM teachers_pairs WHERE name='"+pairName+"' AND teacherID!='0'");) {
					if(rs2.next()) {
						first=false;
					}
				}
				while(rs.next()) {
					if(first) {
						first=false;
						continue;
					}
					sqlExecuteUpdate(conn, "DELETE FROM teachers_pairs WHERE id = '"+rs.getInt(1)+"'");
				}
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}	
	}
	public static void addPairToTeacher(Integer teacherId, String name) throws TimetableException, TeacherException {
		checkMySqlServer();		
		MySqlConnector.getTeacher(teacherId);
		String pairName=TeacherHandler.removeTrash(name);
		if(pairName.length()<1) {
			throw new TeacherException(Type.EMPTY_NAME);
		}
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM teachers_pairs WHERE teacherID='"+teacherId+"' AND "
					+ " name='"+pairName+"'");) {
				if(rs.next()) {
					throw new TeacherException(Type.ALREADY_ADDED);
				} else {
					sqlExecuteUpdate(conn, "INSERT INTO `teachers_pairs` ("
							+ "`id`, "
							+ "`type`, "
							+ "`name`, "
							+ "`teacherID`) "
							+ "VALUES ("
							+ "NULL, "
							+ "'1', "
							+ "'"+pairName+"', "
							+ "'"+teacherId+"');");
							try {
								MySqlConnector.removeUnknownPair(pairName);
							} catch(Exception e) {
								//
							}					
				}
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}		
	}
	private static boolean removeUnknownPair(String pairName) throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM teachers_pairs WHERE teacherID='0' AND "
					+ " name='"+pairName+"'");) {
				if(rs.next()) {
					sqlExecuteUpdate(conn, "DELETE FROM teachers_pairs WHERE id = '"+rs.getInt(1)+"'");
					return true;
				} else {
					return false;
				}
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}			
	}
	public static boolean deleteTeacher(String id) throws NumberFormatException, TimetableException {
		return deleteTeacher(Integer.parseInt(id.trim())); 
	}
	public static void removePairById(String pairID) throws TimetableException {
		checkMySqlServer();
		try {
			sqlExecuteUpdate(conn,"DELETE FROM teachers_pairs WHERE id='"+pairID+"'");
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}		
	}
	public static TeacherPair getPairById(Integer pid) throws TeacherException, TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM teachers_pairs WHERE id='"+pid+"'");) {
				if(rs.next()) {
					return new TeacherPair(rs.getInt(1), rs.getInt(2), rs.getString(3),rs.getInt(4));
				} else {
					throw new TeacherException(Type.BAD_PAIR_ID);
				}
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static void putKeyboardReplaced(Integer peer_id, int i) throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM keyboards WHERE peer_id='"+peer_id+"'");) {
				if(rs.next()) {
					sqlExecuteUpdate(conn, "UPDATE `keyboards` SET "
							+ "replaced = '"+i+"' "
									+ "WHERE peer_id = '"+peer_id+"';");	
				} else {
					sqlExecuteUpdate(conn, "INSERT INTO `keyboards` ("
							+ "`peer_id`, "
							+ "`replaced`) "
							+ "VALUES ("
							+ "'"+peer_id+"', "
							+ "'"+i+"');");				
				}
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static Boolean getKeyboardReplaced(Integer peer_id) throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM keyboards WHERE peer_id='"+peer_id+"'");) {
				if(rs.next()) {
					return rs.getBoolean("replaced");	
				} else {
					sqlExecuteUpdate(conn, "INSERT INTO `keyboards` ("
							+ "`peer_id`, "
							+ "`replaced`) "
							+ "VALUES ("
							+ "'"+peer_id+"', "
							+ "'"+0+"');");		
					return false;
				}
			}
		} catch(SQLException sqle) {
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}
	}
	public static GroupsHistoryObject getGroupHistory(Integer peer_id, Integer user_id, Integer offset, String db_part) throws TimetableException {
		if(offset<0) {
			throw new IllegalArgumentException("Offset can't be < 0");
		}
		Integer limit = (((1+offset)*9))+1;
		checkMySqlServer();
		try {
			GroupsHistoryObject gpo = new GroupsHistoryObject();
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM "+db_part+"_history "
					+ "WHERE "
					+ "keyName='"+peer_id+"_"+user_id+"' "
							+ "LIMIT "+limit);) {
				Integer lCounter = 0;
				Integer newOffset = 0;
				gpo.setGroupNames(new String[9]);
				int i=0;
				while(i<((offset+1)*9)) {
					if(!(rs.next())) {
						break;
					}
					i++;
					if(lCounter>=9) {
						gpo.setGroupNames(new String[9]);
						lCounter = 0;
						newOffset++;
					}
					gpo.getGroupNames()[lCounter] = rs.getString(2);
					lCounter++;
				}
				gpo.setNewOffect(newOffset);
				if(rs.next()) {
					gpo.setNextAvailable(true);
				}
				if(newOffset>0) {
					gpo.setPrevAvailable(true);
				}
			}
			return gpo;
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}		
	}
	public static void removeGroupHistory(Integer peer_id, Integer from_id) throws TimetableException {
		checkMySqlServer();
		try {
			sqlExecuteUpdate(conn, "DELETE FROM group_history WHERE keyName = '"+peer_id+"_"+from_id+"'");
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}	
	}
	public static void removeCabHistory(Integer peer_id, Integer from_id) throws TimetableException {
		checkMySqlServer();
		try {
			sqlExecuteUpdate(conn, "DELETE FROM cab_history WHERE keyName = '"+peer_id+"_"+from_id+"'");
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			disc();
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);			
		}	
	}
	public static void addToCabHistory(Integer peerID, String cabName) throws TimetableException {
		checkMySqlServer();
		try {
			try(ResultSetRaccoon rs = sqlExecuteQuery(conn,"SELECT * FROM cab_history WHERE keyName ='"+peerID+"_"+peerID+"' AND groupName = '"+cabName+"'");) {
				if(rs.next()) {
					//
				} else {
					sqlExecuteUpdate(conn,"INSERT INTO `"+DB_NAME+"`.`cab_history` (`keyName`, `groupName`) VALUES ('"+peerID+"_"+peerID+"','"+cabName+"')");
				}
			}
		} catch(SQLException sqle) {
			disc();		
			class MN {}; throw new TimetableException(ExceptionCode.SQL_UNKWN_ERROR,Foo.getMethodName(MN.class),sqle.getLocalizedMessage(),sqle);
		}
	}
	public static HChanManga getHChanByLink(String link) {
		// TODO:
		return new HChanManga();
    }
	public static void setHCHan(HChanManga comic) {
		// TODO:
	}
}
