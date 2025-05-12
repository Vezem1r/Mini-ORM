package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.db.Database;
import org.example.entity.BadWord;

public class BadWordDao {

  private BadWordDao() {}

  // SQL statements
  private static final String SQL_INSERT = "INSERT INTO BadWords (word) VALUES (?)";
  private static final String SQL_DELETE = "DELETE FROM BadWords WHERE badword_id = ?";
  private static final String SQL_SELECT_BY_ID =
      "SELECT badword_id, word FROM BadWords WHERE badword_id = ?";
  private static final String SQL_SELECT_ALL =
      "SELECT badword_id, word FROM BadWords ORDER BY word";
  private static final String SQL_CHECK_BAD_WORDS =
      "SELECT COUNT(*) FROM BadWords WHERE UPPER(?) LIKE '%' || UPPER(word) || '%'";

  // Column names
  private static final String COL_BADWORD_ID = "badword_id";
  private static final String COL_WORD = "word";

  /** Insert a new bad word */
  public static void insert(BadWord badWord) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      pstmt = conn.prepareStatement(SQL_INSERT, new String[] {COL_BADWORD_ID});

      pstmt.setString(1, badWord.getWord());

      pstmt.executeUpdate();

      // Get generated ID
      rs = pstmt.getGeneratedKeys();
      if (rs.next()) {
        badWord.setBadwordId(rs.getInt(1));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /** Delete a bad word by ID */
  public static void delete(Integer badwordId) throws SQLException {
    Connection conn = Database.getConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE)) {
      pstmt.setInt(1, badwordId);
      pstmt.executeUpdate();
    }
  }

  /** Find bad word by ID */
  public static BadWord findById(Integer badwordId) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    BadWord badWord = null;

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_BY_ID);
      pstmt.setInt(1, badwordId);
      rs = pstmt.executeQuery();

      if (rs.next()) {
        badWord = mapResultSetToBadWord(rs);
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return badWord;
  }

  /** Find all bad words */
  public static List<BadWord> findAll() throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<BadWord> badWords = new ArrayList<>();

    try {
      pstmt = conn.prepareStatement(SQL_SELECT_ALL);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        badWords.add(mapResultSetToBadWord(rs));
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return badWords;
  }

  /** Check if text contains bad words */
  public static boolean containsBadWords(String text) throws SQLException {
    Connection conn = Database.getConnection();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    boolean containsBadWords = false;

    try {
      pstmt = conn.prepareStatement(SQL_CHECK_BAD_WORDS);
      pstmt.setString(1, text);
      rs = pstmt.executeQuery();

      if (rs.next()) {
        containsBadWords = rs.getInt(1) > 0;
      }
    } finally {
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }

    return containsBadWords;
  }

  /** Helper method to map ResultSet to BadWord object */
  private static BadWord mapResultSetToBadWord(ResultSet rs) throws SQLException {
    BadWord badWord = new BadWord();
    badWord.setBadwordId(rs.getInt(COL_BADWORD_ID));
    badWord.setWord(rs.getString(COL_WORD));
    return badWord;
  }
}
