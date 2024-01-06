package com.manage.userbackend.repositories;

import com.manage.userbackend.beans.UserBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserBean, Integer> {
    Optional<UserBean> findByEmail(String email);
}
