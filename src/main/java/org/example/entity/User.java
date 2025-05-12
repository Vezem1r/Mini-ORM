package org.example.entity;

import java.sql.Timestamp;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
  private Integer userId;
  private String username;
  private String email;
  private String password;
  private Timestamp registrationDate;
  private Integer roleId;
}
