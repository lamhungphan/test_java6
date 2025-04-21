package com.java6.repository;

import com.java6.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    User findByFullName(String fullName);
    boolean existsByEmail(String email);
    Page<User> findByFullNameOrEmail(String name, String email, Pageable pageable);
}
