package com.example.zozo.web.repository;

import com.example.zozo.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM User WHERE first_name LIKE %:firstName%", nativeQuery = true)
    List<User> findByFirstNameContaining(@Param("firstName") String firstName);
    //test change
}
