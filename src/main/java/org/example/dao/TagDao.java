package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.db.Database;
import org.example.entity.Tag;

public class TagDao {

  private TagDao() {}

  // SQL statements
  private static final String SQL_INSERT = "INSERT INTO Tag (name) VALUES (?)";
  private static final String SQL_UPDATE = "UPDATE Tag SET name = ? WHERE tag_id = ?";
  private static final String SQL_DELETE = "DELETE FROM Tag WHERE tag_id = ?";
  private static final String SQL_SELECT_BY_ID = "SELECT tag_id, name FROM Tag WHERE tag_id = ?";
  private static final String SQL_SELECT_BY_NAME =
      "SELECT tag_id, name FROM Tag WHERE LOWER(name) = LOWER(?)";
  private static final String SQL_SELECT_ALL = "SELECT tag_id, name FROM Tag ORDER BY name";
  private static final String SQL_SELECT_BY_TOPIC =
      "SELECT t.tag_id, t.name "
          + "FROM Tag t "
          + "JOIN Topic_Tag tt ON t.tag_id = tt.tag_id "
          + "WHERE tt.topic_id = ? "
          + "ORDER BY t.name";

  // Column names
  private static final String COL_TAG_ID = "tag_id";
  private static final String COL_NAME = "name";

  /** Insert a new tag */
  public static void insert(Tag tag) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      pstmt = conn.prepareStatement(SQL_INSERT, new String[] {COL_TAG_ID});

      pstmt.setString(1, tag.getName());

      pstmt.executeUpdate();

      // Get generated ID
      rs = pstmt.getGeneratedKeys();
      if (rs.next()) {
        tag.setTagId(rs.getInt(1));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /** Update an existing tag */
  public static void update(Tag tag) throws SQLException {
    Connection conn = Database.getConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {

      pstmt.setString(1, tag.getName());
      pstmt.setInt(2, tag.getTagId());

      pstmt.executeUpdate();
    }
  }

  /** Delete a tag by ID */
  public static void delete(Integer tagId) throws SQLException {
    Connection conn = Database.getConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE)) {
      pstmt.setInt(1, tagId);
      pstmt.executeUpdate();
    }
  }

  /** Find tag by ID */
  public static Tag findById(Integer tagId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Tag tag = null;

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_ID);
      pstmt.setInt(1, tagId);
      rs = pstmt.executeQuery();

      if (rs.next()) {
        tag = mapResultSetToTag(rs);
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return tag;
  }

  /** Find tag by name */
  public static Tag findByName(String name) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Tag tag = null;

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_NAME);
      pstmt.setString(1, name);
      rs = pstmt.executeQuery();

      if (rs.next()) {
        tag = mapResultSetToTag(rs);
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return tag;
  }

  /** Find all tags */
  public static List<Tag> findAll() throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<Tag> tags = new ArrayList<>();

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_ALL);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        tags.add(mapResultSetToTag(rs));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return tags;
  }

  /** Find all tags for a topic */
  public static List<Tag> findByTopicId(Integer topicId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<Tag> tags = new ArrayList<>();

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_TOPIC);
      pstmt.setInt(1, topicId);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        tags.add(mapResultSetToTag(rs));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return tags;
  }

  /** Helper method to map ResultSet to Tag object */
  private static Tag mapResultSetToTag(ResultSet rs) throws SQLException {
    Tag tag = new Tag();
    tag.setTagId(rs.getInt(COL_TAG_ID));
    tag.setName(rs.getString(COL_NAME));
    return tag;
  }
}
