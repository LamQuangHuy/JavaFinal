package com.example.demo.model.order;

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
public class Orders {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int customerId;
  private int totalPrice;
  private int profit;
  private LocalDateTime createdDate = LocalDateTime.now();;
}
