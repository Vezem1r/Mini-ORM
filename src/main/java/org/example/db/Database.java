package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
  private static final String URL = "jdbc:oracle:thin:@your-host:1521:oracle";
  private static final String USERNAME = "your-username";
  private static final String PASSWORD = "your-password";

  private static Connection connection;

  private Database() {}

  public static Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
      connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    return connection;
  }

  public static void beginTransaction() throws SQLException {
    Connection conn = getConnection();
    conn.setAutoCommit(false);
  }

  public static void endTransaction() throws SQLException {
    Connection conn = getConnection();
    conn.commit();
    conn.setAutoCommit(true);
  }

  public static void rollback() throws SQLException {
    Connection conn = getConnection();
    conn.rollback();
    conn.setAutoCommit(true);
  }

  public static void closeConnection() throws SQLException {
    if (connection != null && !connection.isClosed()) {
      connection.close();
    }
  }
}
