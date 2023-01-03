package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고 있음.
// @Repository가 없어도 IoC 됨. -> 자동으로 빈으로 등록된다.
public interface UserRepository extends JpaRepository<User,Integer> {

    // findBy규칙 -> Username문법
    // select * from user where username = 1?
    public User findByUsername(String username);  // JPA Query Method
}
