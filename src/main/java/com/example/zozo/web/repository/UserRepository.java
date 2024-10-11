package com.example.zozo.web.repository;

import com.example.zozo.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM User WHERE first_name LIKE %:firstName%", nativeQuery = true)
    List<User> findByFirstNameContaining(@Param("firstName") String firstName);

    @Query(value = "SELECT * FROM User WHERE Id = :Id", nativeQuery = true)
    Optional<User> findById(Long Id);

    @Query(value = "SELECT * FROM User WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);
}
