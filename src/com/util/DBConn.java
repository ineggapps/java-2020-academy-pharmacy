package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Singleton Pattern
public class DBConn {
	private static Connection conn;
	private static final String URL = "jdbc:oracle:thin:@sist.sannim.com:1521:xe"; // 11g이하
//	private static final String URL="jdbc:oracle:thin:@//sist.sannim.com:1521/xe";//12c이상
	private static final String USER = "pharmacist";// username은 대소문자를 딱히 구분하지 않음
	private static final String PASSWORD = "java!@#$%";

	private DBConn() {
	}

	public static Connection getConnection() {
		if (conn == null) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver"); // JDK6부터 생략 가능한 구문.
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	public static void close() {
		if (conn != null) {
			try {
				if (!conn.isClosed()) {
					conn.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		conn = null;
	}

	
}
