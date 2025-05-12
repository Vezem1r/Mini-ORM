package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.db.Database;
import org.example.entity.User;

public class UserDao {

  private static final String SQL_INSERT =
      "INSERT INTO \"User\" (username, email, password, registration_date, role_id) "
          + "VALUES (?, ?, ?, ?, ?)";
  private static final String SQL_UPDATE =
      "UPDATE \"User\" SET username = ?, email = ?, password = ?, role_id = ? "
          + "WHERE user_id = ?";
  private static final String SQL_DELETE = "DELETE FROM \"User\" WHERE user_id = ?";
  private static final String SQL_SELECT_ALL =
      "SELECT user_id, username, email, password, registration_date, role_id "
          + "FROM \"User\" ORDER BY user_id";
  private static final String SQL_SELECT_BY_ID =
      "SELECT user_id, username, email, password, registration_date, role_id "
          + "FROM \"User\" WHERE user_id = ?";
  private static final String SQL_SELECT_BY_USERNAME =
      "SELECT user_id, username, email, password, registration_date, role_id "
          + "FROM \"User\" WHERE username = ?";
  private static final String SQL_SELECT_BY_ROLE =
      "SELECT user_id, username, email, password, registration_date, role_id "
          + "FROM \"User\" WHERE role_id = ? ORDER BY username";

  private UserDao() {}

  public static void insert(User user) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      pstmt = conn.prepareStatement(SQL_INSERT, new String[] {"user_id"});

      pstmt.setString(1, user.getUsername());
      pstmt.setString(2, user.getEmail());
      pstmt.setString(3, user.getPassword());
      pstmt.setTimestamp(4, user.getRegistrationDate());
      pstmt.setInt(5, user.getRoleId());

      pstmt.executeUpdate();

      rs = pstmt.getGeneratedKeys();
      if (rs.next()) {
        user.setUserId(rs.getInt(1));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }
  }

  public static void update(User user) throws SQLException {
    Connection conn = Database.getConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {

      pstmt.setString(1, user.getUsername());
      pstmt.setString(2, user.getEmail());
      pstmt.setString(3, user.getPassword());
      pstmt.setInt(4, user.getRoleId());
      pstmt.setInt(5, user.getUserId());

      pstmt.executeUpdate();
    }
  }

  public static void delete(Integer userId) throws SQLException {
    Connection conn = Database.getConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE)) {
      pstmt.setInt(1, userId);
      pstmt.executeUpdate();
    }
  }

  public static User findById(Integer userId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    User user = null;

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_ID);
      pstmt.setInt(1, userId);
      rs = pstmt.executeQuery();

      if (rs.next()) {
        user = mapResultSetToUser(rs);
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return user;
  }

  public static User findByUsername(String username) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    User user = null;

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_USERNAME);
      pstmt.setString(1, username);
      rs = pstmt.executeQuery();

      if (rs.next()) {
        user = mapResultSetToUser(rs);
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return user;
  }

  public static List<User> findAll() throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<User> users = new ArrayList<>();

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_ALL);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        users.add(mapResultSetToUser(rs));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return users;
  }

  public static List<User> findByRole(Integer roleId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<User> users = new ArrayList<>();

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_ROLE);
      pstmt.setInt(1, roleId);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        users.add(mapResultSetToUser(rs));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return users;
  }

  /** Helper method to map ResultSet to User object */
  private static User mapResultSetToUser(ResultSet rs) throws SQLException {
    User user = new User();
    user.setUserId(rs.getInt("user_id"));
    user.setUsername(rs.getString("username"));
    user.setEmail(rs.getString("email"));
    user.setPassword(rs.getString("password"));
    user.setRegistrationDate(rs.getTimestamp("registration_date"));
    user.setRoleId(rs.getInt("role_id"));
    return user;
  }
}
