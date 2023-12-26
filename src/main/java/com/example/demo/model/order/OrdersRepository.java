package com.example.demo.model.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends CrudRepository<Orders, Integer> {
  Iterable<Orders> findByCustomerId(int customerId);

  Orders findById(int id);

  @Query("SELECT e FROM Orders e WHERE e.createdDate BETWEEN :startDate AND :endDate")
  List<Orders> findAllByDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

  @Query("SELECT e FROM Orders e WHERE Date(e.createdDate) = DATE(:date)")
  List<Orders> findAllByDate(@Param("date") LocalDateTime date);
}
