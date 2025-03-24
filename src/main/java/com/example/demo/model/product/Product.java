package com.example.demo.model.product;

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
public class Product {
  public Product(String barcode, String name, String category, int importPrice, int retailPrice) {
    this.barcode = barcode;
    this.name = name;
    this.category = category;
    this.importPrice = importPrice;
    this.retailPrice = retailPrice;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  private String barcode;
  private String category;
  private int importPrice;
  private int retailPrice;
  private LocalDateTime createdDate = LocalDateTime.now();;
}
