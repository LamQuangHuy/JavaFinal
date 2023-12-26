package com.example.demo.model.account;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account {
  public Account(String name, String email) {
    this.name = name;
    this.email = email;
    this.password = email.split("@")[0];
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  private String avatar = "https://firebasestorage.googleapis.com/v0/b/phone-c4bc5.appspot.com/o/default_avatar.jpg?alt=media&token=0ff85744-9209-457b-aaf8-66d1f6893155";
  private String phone;
  private String address;
  private String email;
  private String password;
  private String role; // user | customer | admin
  private String status = "new"; // new | active | locked
  private LocalDateTime createdDate = LocalDateTime.now();;
}
