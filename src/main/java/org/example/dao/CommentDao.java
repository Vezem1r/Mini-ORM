package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.db.Database;
import org.example.entity.Comment;

public class CommentDao {

  private CommentDao() {}

  // SQL statements
  private static final String SQL_INSERT =
      "INSERT INTO Comment_ (content, created_at, user_id, topic_id) "
          + "VALUES (?, SYSTIMESTAMP, ?, ?)";
  private static final String SQL_UPDATE = "UPDATE Comment_ SET content = ? WHERE comment_id = ?";
  private static final String SQL_SOFT_DELETE =
      "UPDATE Comment_ SET deleted_at = SYSTIMESTAMP WHERE comment_id = ?";
  private static final String SQL_SELECT_BY_ID =
      "SELECT comment_id, content, created_at, deleted_at, user_id, topic_id "
          + "FROM Comment_ WHERE comment_id = ?";
  private static final String SQL_SELECT_BY_TOPIC =
      "SELECT comment_id, content, created_at, deleted_at, user_id, topic_id "
          + "FROM Comment_ WHERE topic_id = ? AND deleted_at IS NULL "
          + "ORDER BY created_at";
  private static final String SQL_SELECT_BY_USER =
      "SELECT comment_id, content, created_at, deleted_at, user_id, topic_id "
          + "FROM Comment_ WHERE user_id = ? AND deleted_at IS NULL "
          + "ORDER BY created_at DESC";
  private static final String SQL_SELECT_CREATED_AT =
      "SELECT created_at FROM Comment_ WHERE comment_id = ?";

  // Column names
  private static final String COL_COMMENT_ID = "comment_id";
  private static final String COL_CONTENT = "content";
  private static final String COL_CREATED_AT = "created_at";
  private static final String COL_DELETED_AT = "deleted_at";
  private static final String COL_USER_ID = "user_id";
  private static final String COL_TOPIC_ID = "topic_id";

  /** Insert a new comment */
  public static void insert(Comment comment) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      pstmt = conn.prepareStatement(SQL_INSERT, new String[] {COL_COMMENT_ID});

      pstmt.setString(1, comment.getContent());
      pstmt.setInt(2, comment.getUserId());
      pstmt.setInt(3, comment.getTopicId());

      pstmt.executeUpdate();

      // Get generated ID and timestamp
      rs = pstmt.getGeneratedKeys();
      if (rs.next()) {
        comment.setCommentId(rs.getInt(1));
      }

      // Get the created timestamp
      pstmt.close();
      pstmt = conn.prepareStatement(SQL_SELECT_CREATED_AT);
      pstmt.setInt(1, comment.getCommentId());
      rs = pstmt.executeQuery();
      if (rs.next()) {
        comment.setCreatedAt(rs.getTimestamp(1));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /** Update an existing comment */
  public static void update(Comment comment) throws SQLException {
    Connection conn = Database.getConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {

      pstmt.setString(1, comment.getContent());
      pstmt.setInt(2, comment.getCommentId());

      pstmt.executeUpdate();
    }
  }

  /** Soft delete a comment */
  public static void softDelete(Integer commentId) throws SQLException {
    Connection conn = Database.getConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(SQL_SOFT_DELETE)) {
      pstmt.setInt(1, commentId);
      pstmt.executeUpdate();
    }
  }

  /** Find comment by ID */
  public static Comment findById(Integer commentId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Comment comment = null;

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_ID);
      pstmt.setInt(1, commentId);
      rs = pstmt.executeQuery();

      if (rs.next()) {
        comment = mapResultSetToComment(rs);
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return comment;
  }

  /** Find all comments for a topic */
  public static List<Comment> findByTopicId(Integer topicId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<Comment> comments = new ArrayList<>();

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_TOPIC);
      pstmt.setInt(1, topicId);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        comments.add(mapResultSetToComment(rs));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return comments;
  }

  /** Find all comments by user */
  public static List<Comment> findByUserId(Integer userId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<Comment> comments = new ArrayList<>();

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_USER);
      pstmt.setInt(1, userId);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        comments.add(mapResultSetToComment(rs));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return comments;
  }

  /** Helper method to map ResultSet to Comment object */
  private static Comment mapResultSetToComment(ResultSet rs) throws SQLException {
    Comment comment = new Comment();
    comment.setCommentId(rs.getInt(COL_COMMENT_ID));
    comment.setContent(rs.getString(COL_CONTENT));
    comment.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
    comment.setDeletedAt(rs.getTimestamp(COL_DELETED_AT));
    comment.setUserId(rs.getInt(COL_USER_ID));
    comment.setTopicId(rs.getInt(COL_TOPIC_ID));
    return comment;
  }
}
