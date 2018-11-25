package com.beardedtom.db.dao;

import com.beardedtom.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Long> {
    User findUserByUserEmail(String userEmail);
}
