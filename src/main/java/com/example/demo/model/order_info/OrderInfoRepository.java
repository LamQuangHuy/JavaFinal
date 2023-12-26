package com.example.demo.model.order_info;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.product.Product;

@Repository
public interface OrderInfoRepository extends CrudRepository<OrderInfo, Integer> {
  @Query("SELECT a FROM Product a, OrderInfo o WHERE a.id = o.productId and o.orderId = :orderId")
  Iterable<Product> getProductsByOrderId(@Param("orderId") int orderId);

  List<OrderInfo> findAllByOrderId(int orderId);

}
