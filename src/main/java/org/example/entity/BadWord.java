package org.example.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BadWord {
  private Integer badwordId;
  private String word;
}
