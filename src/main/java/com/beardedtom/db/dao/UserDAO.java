package com.beardedtom.db.dao;

import com.beardedtom.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<User, Long> {
    User findUserByUserEmail(String userEmail);
}
