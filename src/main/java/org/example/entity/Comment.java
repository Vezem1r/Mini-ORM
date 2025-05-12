package org.example.entity;

import java.sql.Timestamp;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Comment {
  private Integer commentId;
  private String content;
  private Timestamp createdAt;
  private Timestamp deletedAt;
  private Integer userId;
  private Integer topicId;
}
