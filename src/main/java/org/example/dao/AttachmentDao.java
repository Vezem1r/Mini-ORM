package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.db.Database;
import org.example.entity.Attachment;

public class AttachmentDao {

  private AttachmentDao() {}

  // SQL statements
  private static final String SQL_INSERT =
      "INSERT INTO Attachment (file_path, uploaded_at, comment_id, topic_id) "
          + "VALUES (?, SYSTIMESTAMP, ?, ?)";
  private static final String SQL_DELETE = "DELETE FROM Attachment WHERE attachment_id = ?";
  private static final String SQL_SELECT_BY_ID =
      "SELECT attachment_id, file_path, uploaded_at, comment_id, topic_id "
          + "FROM Attachment WHERE attachment_id = ?";
  private static final String SQL_SELECT_BY_TOPIC =
      "SELECT attachment_id, file_path, uploaded_at, comment_id, topic_id "
          + "FROM Attachment WHERE topic_id = ? ORDER BY uploaded_at";
  private static final String SQL_SELECT_BY_COMMENT =
      "SELECT attachment_id, file_path, uploaded_at, comment_id, topic_id "
          + "FROM Attachment WHERE comment_id = ? ORDER BY uploaded_at";
  private static final String SQL_SELECT_UPLOADED_AT =
      "SELECT uploaded_at FROM Attachment WHERE attachment_id = ?";

  // Column names
  private static final String COL_ATTACHMENT_ID = "attachment_id";
  private static final String COL_FILE_PATH = "file_path";
  private static final String COL_UPLOADED_AT = "uploaded_at";
  private static final String COL_COMMENT_ID = "comment_id";
  private static final String COL_TOPIC_ID = "topic_id";

  /** Insert a new attachment */
  public static void insert(Attachment attachment) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      pstmt = conn.prepareStatement(SQL_INSERT, new String[] {COL_ATTACHMENT_ID});

      pstmt.setString(1, attachment.getFilePath());
      pstmt.setNull(2, Types.INTEGER); // comment_id can be null
      pstmt.setNull(3, Types.INTEGER); // topic_id can be null

      // Set either comment_id or topic_id
      if (attachment.getCommentId() != null) {
        pstmt.setInt(2, attachment.getCommentId());
      } else if (attachment.getTopicId() != null) {
        pstmt.setInt(3, attachment.getTopicId());
      }

      pstmt.executeUpdate();

      // Get generated ID and timestamp
      rs = pstmt.getGeneratedKeys();
      if (rs.next()) {
        attachment.setAttachmentId(rs.getInt(1));
      }

      // Get the upload timestamp
      pstmt.close();
      pstmt = conn.prepareStatement(SQL_SELECT_UPLOADED_AT);
      pstmt.setInt(1, attachment.getAttachmentId());
      rs = pstmt.executeQuery();
      if (rs.next()) {
        attachment.setUploadedAt(rs.getTimestamp(1));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /** Delete an attachment by ID */
  public static void delete(Integer attachmentId) throws SQLException {
    Connection conn = Database.getConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE)) {
      pstmt.setInt(1, attachmentId);
      pstmt.executeUpdate();
    }
  }

  /** Find attachment by ID */
  public static Attachment findById(Integer attachmentId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Attachment attachment = null;

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_ID);
      pstmt.setInt(1, attachmentId);
      rs = pstmt.executeQuery();

      if (rs.next()) {
        attachment = mapResultSetToAttachment(rs);
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return attachment;
  }

  /** Find all attachments for a topic */
  public static List<Attachment> findByTopicId(Integer topicId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<Attachment> attachments = new ArrayList<>();

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_TOPIC);
      pstmt.setInt(1, topicId);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        attachments.add(mapResultSetToAttachment(rs));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return attachments;
  }

  /** Find all attachments for a comment */
  public static List<Attachment> findByCommentId(Integer commentId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<Attachment> attachments = new ArrayList<>();

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_COMMENT);
      pstmt.setInt(1, commentId);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        attachments.add(mapResultSetToAttachment(rs));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return attachments;
  }

  /** Helper method to map ResultSet to Attachment object */
  private static Attachment mapResultSetToAttachment(ResultSet rs) throws SQLException {
    Attachment attachment = new Attachment();
    attachment.setAttachmentId(rs.getInt(COL_ATTACHMENT_ID));
    attachment.setFilePath(rs.getString(COL_FILE_PATH));
    attachment.setUploadedAt(rs.getTimestamp(COL_UPLOADED_AT));

    int commentId = rs.getInt(COL_COMMENT_ID);
    attachment.setCommentId(rs.wasNull() ? null : commentId);

    int topicId = rs.getInt(COL_TOPIC_ID);
    attachment.setTopicId(rs.wasNull() ? null : topicId);

    return attachment;
  }
}
