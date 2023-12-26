package com.example.demo.model.product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

  boolean existsByBarcode(String barcode);

  Product findById(int id);
}
