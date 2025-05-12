package org.example.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Category {
  private Integer categoryId;
  private String name;
  private String description;
}
