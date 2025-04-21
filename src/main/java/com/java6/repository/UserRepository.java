package com.java6.repository;

import com.java6.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    User findByFullName(String fullName);
    boolean existsByEmail(String email);

    @Query(
            value = "SELECT u FROM User u WHERE u.fullName LIKE %:keyword% OR u.email LIKE %:keyword%",
            countQuery = "SELECT COUNT(u) FROM User u WHERE u.fullName LIKE %:keyword% OR u.email LIKE %:keyword%"
    )
    Page<User> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Page<User> findByFullNameOrEmail(String name, String email, Pageable pageable);
}
