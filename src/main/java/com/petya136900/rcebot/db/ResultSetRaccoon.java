package com.petya136900.rcebot.db;

import java.sql.ResultSet;
import java.sql.SQLException;
public class ResultSetRaccoon implements AutoCloseable {
	ResultSet rs;
	public ResultSetRaccoon(ResultSet rs) {
		this.rs=rs;
	}

	@Override
    public void close() {
		try {
			rs.getStatement().close();
		} catch(SQLException ignored) {}
		try {
			rs.close();
		} catch(SQLException ignored) {}
    }

	public boolean next() throws SQLException {
		return rs.next();
	}

	public String getString(String columnLabel) throws SQLException {
		return rs.getString(columnLabel);
	}

	public Integer getInt(String columnLabel) throws SQLException {
		return rs.getInt(columnLabel);
	}
	public Integer getInt(int colNum) throws SQLException {
		return rs.getInt(colNum);
	}
	public Boolean getBoolean(String columnLabel) throws SQLException {
		return rs.getBoolean(columnLabel);
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
	public Long getLong(String columnLabel) throws SQLException {
		return rs.getLong(columnLabel);
	}
}
