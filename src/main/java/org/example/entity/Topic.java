package org.example.entity;

import java.sql.Timestamp;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Topic {
  private Integer topicId;
  private String title;
  private String content;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private Integer userId;
  private Integer categoryId;
  private Timestamp deletedAt;
}
