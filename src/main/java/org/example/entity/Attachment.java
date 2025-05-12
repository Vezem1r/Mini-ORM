package org.example.entity;

import java.sql.Timestamp;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Attachment {
  private Integer attachmentId;
  private String filePath;
  private Timestamp uploadedAt;
  private Integer commentId;
  private Integer topicId;
}
