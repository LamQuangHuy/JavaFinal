package com.example.demo.model.order_info;

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
public class OrderInfo {
  public OrderInfo(int productId, int orderId, int customerId, int quantity, int totalPrice) {
    this.productId = productId;
    this.orderId = orderId;
    this.customerId = customerId;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int productId; // likes '1,2,3,4'
  private int orderId;
  private int customerId;
  private int quantity;
  private int totalPrice;
  private LocalDateTime createdDate = LocalDateTime.now();;
}
