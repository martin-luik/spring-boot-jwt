package com.example.demo.repository;

import com.example.demo.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    UserAccount findUserAccountByUsername(String username);
}
