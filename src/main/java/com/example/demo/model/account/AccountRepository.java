package com.example.demo.model.account;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

  @Query("SELECT a FROM Account a WHERE a.email like CONCAT(:username, '@%')")
  Account findByUsername(@Param("username") String username);

  Account findById(int id);

  Account findByEmail(String email);

  @Query("SELECT a FROM Account a, Orders o WHERE o.customerId = a.id and o.id = :orderId")
  Account findByOrderId(@Param("orderId") int orderId);

  @Query("SELECT a FROM Account a WHERE a.phone = :phone and a.role = 'customer'")
  Account findCustomerByPhone(@Param("phone") String phone);

  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);

}
