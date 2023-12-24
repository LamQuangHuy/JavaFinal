package com.example.demo.repository;

import com.example.demo.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
    
    List<Account> findAll();

    Account findByUsername(String username);
}
