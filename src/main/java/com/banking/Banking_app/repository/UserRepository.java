package com.banking.Banking_app.repository;

import com.banking.Banking_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


public interface UserRepository extends JpaRepository<User , Long> {
}
