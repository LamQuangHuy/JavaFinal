package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AccountService {

    @Autowired
    private AccountRepository repo;

    public Account addAccount(@RequestBody Account account) {
        if (account.getRole() == null) {
            account.setRole("user");
        }

        //account.setPassword(encoder.encode(account.getPassword())); // hash
        repo.save(account);
        return account;
    }
    public Account findById(int id)
    {
        return repo.findById(id).orElse(null);
    }

    public Account findByUsername(String username)
    {
        return repo.findByUsername(username);
    }

    public List<Account> getAllAccount() {
        return repo.findAll();
    }

    public void changePassword(Account account,String oldPassword, String newPassword) throws Exception {
        Account dbAccount = repo.findById(account.getId()).get();
        if(!Objects.equals(dbAccount.getPassword(), oldPassword)) {
            throw new Exception("Mật khẩu cũ không chính xác");
        }

        // Mã hóa mật khẩu mới


        // Cập nhật mật khẩu mới lên DB
        dbAccount.setPassword(newPassword);
        repo.save(dbAccount);

    }

    public void sendGmail(Account account)
    {
        //
    }
}


