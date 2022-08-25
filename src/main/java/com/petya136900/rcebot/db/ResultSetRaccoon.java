package com.petya136900.rcebot.db;

import java.sql.ResultSet;
import java.sql.SQLException;
public class ResultSetRaccoon implements AutoCloseable {
	ResultSet rs;
	public ResultSetRaccoon(ResultSet rs) {
		this.rs=rs;
	}

	@Override
    public void close() throws SQLException {
		try {
			rs.getStatement().close();
		} catch(SQLException sqle) {
			//
		}
		try {
			rs.close();
		} catch(SQLException sqle) {
			//
		}		
    }

	public boolean next() throws SQLException {
		if(rs.next()) {
			return true; 
		} else {
			return false;
		}
	}

	public String getString(String string) throws SQLException {
		return rs.getString(string);
	}

	public Integer getInt(String string) throws SQLException {
		return rs.getInt(string);
	}
	public Integer getInt(int colNum) throws SQLException {
		return rs.getInt(colNum);
	}
	public Boolean getBoolean(String string) throws SQLException {
		return rs.getBoolean(string);
	}
	public Boolean getBoolean(int colNum) throws SQLException {
		return rs.getBoolean(colNum);
	}
	public ResultSet getRs() {
		return rs;
	}
	public String getString(int colNum) throws SQLException {
		return rs.getString(colNum);
	}
}
