package org.example;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.example.dao.*;
import org.example.db.Database;
import org.example.entity.*;
import org.example.util.ColoredFormatter;

public class Main {

  private static final Logger logger = Logger.getLogger(Main.class.getName());

  static {
    logger.setUseParentHandlers(false);
    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setFormatter(new ColoredFormatter());
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);
    logger.setLevel(Level.ALL);
  }

  public static void main(String[] args) {
    try {
      // Test 1: Create topic using stored procedure
      logger.info("=== Test 1: Creating topic using stored procedure ===");
      testCreateTopicWithProcedure();

      // Test 2: Create topic using SQL statements in transaction
      logger.info("=== Test 2: Creating topic using SQL statements in transaction ===");
      testCreateTopicWithSqlStatements();

      // Test 3: Attempt to create topic with bad words (should fail)
      logger.info("=== Test 3: Attempting to create topic with bad words ===");
      testCreateTopicWithBadWords();

      // Test 4: Demonstrate other DAO operations
      logger.info("=== Test 4: Demonstrating other DAO operations ===");
      otherDaoOperations();

    } catch (SQLException e) {
      logger.log(Level.SEVERE, "Database error: " + e.getMessage(), e);
    } finally {
      try {
        Database.closeConnection();
      } catch (SQLException e) {
        logger.log(Level.SEVERE, "Error closing connection: " + e.getMessage(), e);
      }
    }
  }

  /** Test creating topic using stored procedure */
  private static void testCreateTopicWithProcedure() throws SQLException {
    logger.info("Creating topic using stored procedure...");

    Topic topic = new Topic();
    topic.setTitle("How to start with Java programming");
    topic.setContent("I want to learn Java. Do you have any tips for beginners?");
    topic.setUserId(4);
    topic.setCategoryId(3); // Programming category

    List<String> tags = Arrays.asList("java", "programming", "beginner");
    String attachmentFilename = "java_guide.pdf";
    String attachmentPath = "/uploads";

    boolean success =
        TopicDao.createUsingProcedure(topic, tags, attachmentFilename, attachmentPath);

    if (success) {
      logger.info("Topic successfully created with ID: " + topic.getTopicId());
      printTopicDetails(topic.getTopicId());
    } else {
      logger.warning("Failed to create topic.");
    }
  }

  /** Test creating topic using SQL statements in transaction */
  private static void testCreateTopicWithSqlStatements() throws SQLException {
    logger.info("Creating topic using SQL statements in transaction...");

    Topic topic = new Topic();
    topic.setTitle("AGAGAGAGAGAGDASJDSFHSDFKDSFDSFKLDSKLDSGHDSGKL");
    topic.setContent(
        "What IDE do you recommend for Python development? I'm using PyCharm but interested in alternatives.");
    topic.setUserId(5);
    topic.setCategoryId(3); // Programming category

    List<String> tags = Arrays.asList("python", "ide", "development");
    String attachmentFilename = "python_ides.xlsx";
    String attachmentPath = "/uploads";

    boolean success =
        TopicDao.createUsingSqlStatements(topic, tags, attachmentFilename, attachmentPath);

    if (success) {
      logger.info("Topic successfully created with ID: " + topic.getTopicId());
      printTopicDetails(topic.getTopicId());
    } else {
      logger.warning("Failed to create topic.");
    }
  }

  /** Test creating topic with bad words (should fail) */
  private static void testCreateTopicWithBadWords() throws SQLException {
    logger.info("Attempting to create topic with bad content...");

    Topic topic = new Topic();
    topic.setTitle("Test spam topic");
    topic.setContent("This is a test topic containing spam and inappropriate content.");
    topic.setUserId(6);
    topic.setCategoryId(1); // General category

    List<String> tags = Arrays.asList("test", "spam");

    try {
      // Try with procedure - should fail
      logger.info("Attempt using procedure:");
      boolean success1 = TopicDao.createUsingProcedure(topic, tags, null, null);
      logger.info("Result: " + (success1 ? "Success" : "Failed (expected)"));

      topic.setTopicId(null);

      logger.info("Attempt using SQL statements:");
      boolean success2 = TopicDao.createUsingSqlStatements(topic, tags, null, null);
      logger.info("Result: " + (success2 ? "Success" : "Failed (expected)"));

    } catch (SQLException e) {
      logger.info("Creation correctly failed due to inappropriate content.");
      logger.info("Error: " + e.getMessage());
    }
  }

  /** Demonstrate other DAO operations */
  private static void otherDaoOperations() throws SQLException {
    logger.info("Demonstrating other DAO operations...");

    // Find user
    User user = UserDao.findById(4);
    if (user != null) {
      logger.info("Found user: " + user.getUsername());
    }

    // Find all topics in category
    List<Topic> topics = TopicDao.findByCategoryId(3);
    logger.info("Number of topics in Programming category: " + topics.size());

    // Find comments for topic
    if (!topics.isEmpty()) {
      Topic firstTopic = topics.getFirst();
      List<Comment> comments = CommentDao.findByTopicId(firstTopic.getTopicId());
      logger.info("Number of comments on first topic: " + comments.size());
    }

    // Find all tags
    List<Tag> allTags = TagDao.findAll();
    logger.info("Total number of tags: " + allTags.size());

    // Check text for bad words
    boolean hasBadWords = BadWordDao.containsBadWords("This is clean text without problems");
    logger.info("Text contains bad words: " + hasBadWords);

    hasBadWords = BadWordDao.containsBadWords("This is text with spam content");
    logger.info("Text with spam contains bad words: " + hasBadWords);
  }

  /** Helper method to print topic details */
  private static void printTopicDetails(Integer topicId) throws SQLException {
    Topic topic = TopicDao.findById(topicId);
    if (topic != null) {
      logger.info("  Title: " + topic.getTitle());
      logger.info(
          "  Content: "
              + topic.getContent().substring(0, Math.min(50, topic.getContent().length()))
              + "...");
      logger.info("  Created: " + topic.getCreatedAt());

      // Tags
      List<Tag> tags = TagDao.findByTopicId(topicId);
      if (!tags.isEmpty()) {
        StringBuilder logMessage = new StringBuilder("  Tags: ");
        for (Tag tag : tags) {
          logMessage.append(tag.getName()).append(" ");
        }
        logger.info(logMessage.toString());
      }

      // Attachments
      List<Attachment> attachments = AttachmentDao.findByTopicId(topicId);
      if (!attachments.isEmpty()) {
        logger.info("  Attachments: " + attachments.size());
        for (Attachment attachment : attachments) {
          logger.info("    - " + attachment.getFilePath());
        }
      }

      // Comments
      List<Comment> comments = CommentDao.findByTopicId(topicId);
      logger.info("  Number of comments: " + comments.size());
    }
  }
}
