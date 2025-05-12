package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.db.Database;
import org.example.entity.Attachment;
import org.example.entity.Comment;
import org.example.entity.Tag;
import org.example.entity.Topic;

public class TopicDao {

  private TopicDao() {}

  // SQL statements
  private static final String SQL_INSERT =
      "INSERT INTO Topic (title, content, created_at, user_id, category_id) "
          + "VALUES (?, ?, SYSTIMESTAMP, ?, ?)";
  private static final String SQL_UPDATE =
      "UPDATE Topic SET title = ?, content = ?, updated_at = SYSTIMESTAMP " + "WHERE topic_id = ?";
  private static final String SQL_SOFT_DELETE =
      "UPDATE Topic SET deleted_at = SYSTIMESTAMP WHERE topic_id = ?";
  private static final String SQL_SELECT_ALL =
      "SELECT topic_id, title, content, created_at, updated_at, user_id, category_id, deleted_at "
          + "FROM Topic WHERE deleted_at IS NULL ORDER BY created_at DESC";
  private static final String SQL_SELECT_BY_CATEGORY =
      "SELECT topic_id, title, content, created_at, updated_at, user_id, category_id, deleted_at "
          + "FROM Topic WHERE category_id = ? AND deleted_at IS NULL ORDER BY created_at DESC";
  private static final String SQL_SELECT_BY_USER =
      "SELECT topic_id, title, content, created_at, updated_at, user_id, category_id, deleted_at "
          + "FROM Topic WHERE user_id = ? AND deleted_at IS NULL ORDER BY created_at DESC";
  private static final String SQL_SELECT_CREATED_AT =
      "SELECT created_at FROM Topic WHERE topic_id = ?";
  private static final String SQL_SELECT_UPDATED_AT =
      "SELECT updated_at FROM Topic WHERE topic_id = ?";
  private static final String SQL_SELECT_BY_ID =
      "SELECT topic_id, title, content, created_at, updated_at, user_id, category_id, deleted_at "
          + "FROM Topic WHERE topic_id = ?";

  // Column names
  private static final String COL_TOPIC_ID = "topic_id";
  private static final String COL_TITLE = "title";
  private static final String COL_CONTENT = "content";
  private static final String COL_CREATED_AT = "created_at";
  private static final String COL_UPDATED_AT = "updated_at";
  private static final String COL_USER_ID = "user_id";
  private static final String COL_CATEGORY_ID = "category_id";
  private static final String COL_DELETED_AT = "deleted_at";

  /** Create topic using stored procedure */
  public static boolean createUsingProcedure(
      Topic topic, List<String> tags, String attachmentFilename, String attachmentPath)
      throws SQLException {
    Connection conn = Database.getConnection();
    CallableStatement cstmt = null;

    try {
      // Begin transaction
      Database.beginTransaction();

      // Call stored procedure
      cstmt = conn.prepareCall("{call InsertTopicWithDetails(?, ?, ?, ?, ?, ?, ?, ?, ?)}");

      // OUT parameter for topic_id
      cstmt.registerOutParameter(1, Types.INTEGER);

      // IN parameters
      cstmt.setInt(2, topic.getCategoryId());
      cstmt.setInt(3, topic.getUserId());
      cstmt.setString(4, topic.getTitle());

      java.sql.Clob clob = conn.createClob();
      clob.setString(1, topic.getContent());
      cstmt.setClob(5, clob);

      if (tags != null && !tags.isEmpty()) {
        oracle.sql.ArrayDescriptor descriptor =
            oracle.sql.ArrayDescriptor.createDescriptor("VARCHAR2_ARRAY", conn);
        oracle.sql.ARRAY array =
            new oracle.sql.ARRAY(descriptor, conn, tags.toArray(new String[0]));
        cstmt.setArray(6, array);
      } else {
        cstmt.setNull(6, Types.ARRAY);
      }

      // Attachment parameters
      cstmt.setString(7, attachmentFilename);
      cstmt.setString(8, attachmentPath);

      // OUT parameter for success
      cstmt.registerOutParameter(9, Types.INTEGER);

      // Execute procedure
      cstmt.execute();

      // Get OUT parameters
      topic.setTopicId(cstmt.getInt(1));
      boolean success = (cstmt.getInt(9) == 1);

      if (success) {
        Database.endTransaction();
      } else {
        Database.rollback();
      }

      return success;

    } catch (SQLException e) {
      Database.rollback();
      throw e;
    } finally {
      if (cstmt != null) cstmt.close();
    }
  }

  /** Create topic using SQL statements in transaction */
  public static boolean createUsingSqlStatements(
      Topic topic, List<String> tags, String attachmentFilename, String attachmentPath)
      throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      // Begin transaction
      Database.beginTransaction();

      // Step 1: Insert topic
      pstmt = conn.prepareStatement(SQL_INSERT, new String[] {COL_TOPIC_ID});
      pstmt.setString(1, topic.getTitle());
      pstmt.setString(2, topic.getContent());
      pstmt.setInt(3, topic.getUserId());
      pstmt.setInt(4, topic.getCategoryId());

      pstmt.executeUpdate();

      // Get generated topic ID
      rs = pstmt.getGeneratedKeys();
      if (rs.next()) {
        topic.setTopicId(rs.getInt(1));
      }
      rs.close();

      // Get timestamp
      pstmt.close();
      pstmt = conn.prepareStatement(SQL_SELECT_CREATED_AT);
      pstmt.setInt(1, topic.getTopicId());
      rs = pstmt.executeQuery();
      if (rs.next()) {
        topic.setCreatedAt(rs.getTimestamp(1));
      }
      rs.close();
      pstmt.close();

      // Step 2: Check for bad words
      if (BadWordDao.containsBadWords(topic.getTitle())
          || BadWordDao.containsBadWords(topic.getContent())) {
        Database.rollback();
        return false;
      }

      // Step 3: Process tags
      if (tags != null && !tags.isEmpty()) {
        for (String tagName : tags) {
          // Check if tag exists
          Tag tag = TagDao.findByName(tagName);

          // Create tag if it doesn't exist
          if (tag == null) {
            tag = new Tag();
            tag.setName(tagName);
            TagDao.insert(tag);
          }

          // Create topic-tag relationship
          pstmt = conn.prepareStatement("INSERT INTO Topic_Tag (topic_id, tag_id) VALUES (?, ?)");
          pstmt.setInt(1, topic.getTopicId());
          pstmt.setInt(2, tag.getTagId());

          try {
            pstmt.executeUpdate();
          } catch (SQLException e) {
            if (!e.getMessage().contains("unique constraint")) {
              throw e;
            }
          }
          pstmt.close();
        }
      }

      // Step 4: Process attachment
      if (attachmentFilename != null && !attachmentFilename.isEmpty()) {
        Attachment attachment = new Attachment();
        String fullPath = attachmentPath + "/" + topic.getTopicId() + "_" + attachmentFilename;
        attachment.setFilePath(fullPath);
        attachment.setTopicId(topic.getTopicId());
        AttachmentDao.insert(attachment);
      }

      // Step 5: Create first comment
      Comment comment = new Comment();
      comment.setContent(topic.getContent());
      comment.setUserId(topic.getUserId());
      comment.setTopicId(topic.getTopicId());
      CommentDao.insert(comment);

      // Commit transaction
      Database.endTransaction();
      return true;

    } catch (SQLException e) {
      Database.rollback();
      throw e;
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /** Find topic by ID */
  public static Topic findById(Integer topicId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Topic topic = null;

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_ID);
      pstmt.setInt(1, topicId);
      rs = pstmt.executeQuery();

      if (rs.next()) {
        topic = mapResultSetToTopic(rs);
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return topic;
  }

  /** Insert a new topic */
  public static void insert(Topic topic) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      pstmt = conn.prepareStatement(SQL_INSERT, new String[] {COL_TOPIC_ID});

      pstmt.setString(1, topic.getTitle());
      pstmt.setString(2, topic.getContent());
      pstmt.setInt(3, topic.getUserId());
      pstmt.setInt(4, topic.getCategoryId());

      pstmt.executeUpdate();

      // Get generated ID
      rs = pstmt.getGeneratedKeys();
      if (rs.next()) {
        topic.setTopicId(rs.getInt(1));
      }

      // Get the created timestamp
      pstmt.close();
      pstmt = conn.prepareStatement(SQL_SELECT_CREATED_AT);
      pstmt.setInt(1, topic.getTopicId());
      rs = pstmt.executeQuery();
      if (rs.next()) {
        topic.setCreatedAt(rs.getTimestamp(1));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /** Update an existing topic */
  public static void update(Topic topic) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;

    try {
      pstmt = conn.prepareStatement(SQL_UPDATE);

      pstmt.setString(1, topic.getTitle());
      pstmt.setString(2, topic.getContent());
      pstmt.setInt(3, topic.getTopicId());

      pstmt.executeUpdate();

      // Get the updated timestamp
      pstmt.close();
      pstmt = conn.prepareStatement(SQL_SELECT_UPDATED_AT);
      pstmt.setInt(1, topic.getTopicId());
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        topic.setUpdatedAt(rs.getTimestamp(1));
      }
    } finally {
      if (pstmt != null) pstmt.close();
    }
  }

  /** Soft delete a topic */
  public static void softDelete(Integer topicId) throws SQLException {
    Connection conn = Database.getConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(SQL_SOFT_DELETE)) {
      pstmt.setInt(1, topicId);
      pstmt.executeUpdate();
    }
  }

  /** Find all topics */
  public static List<Topic> findAll() throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<Topic> topics = new ArrayList<>();

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_ALL);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        topics.add(mapResultSetToTopic(rs));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return topics;
  }

  /** Find topics by category */
  public static List<Topic> findByCategoryId(Integer categoryId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<Topic> topics = new ArrayList<>();

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_CATEGORY);
      pstmt.setInt(1, categoryId);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        topics.add(mapResultSetToTopic(rs));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return topics;
  }

  /** Find topics by user */
  public static List<Topic> findByUserId(Integer userId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<Topic> topics = new ArrayList<>();

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_USER);
      pstmt.setInt(1, userId);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        topics.add(mapResultSetToTopic(rs));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return topics;
  }

  /** Helper method to map ResultSet to Topic object */
  private static Topic mapResultSetToTopic(ResultSet rs) throws SQLException {
    Topic topic = new Topic();
    topic.setTopicId(rs.getInt(COL_TOPIC_ID));
    topic.setTitle(rs.getString(COL_TITLE));
    topic.setContent(rs.getString(COL_CONTENT));
    topic.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
    topic.setUpdatedAt(rs.getTimestamp(COL_UPDATED_AT));
    topic.setUserId(rs.getInt(COL_USER_ID));
    topic.setCategoryId(rs.getInt(COL_CATEGORY_ID));
    topic.setDeletedAt(rs.getTimestamp(COL_DELETED_AT));
    return topic;
  }
}
