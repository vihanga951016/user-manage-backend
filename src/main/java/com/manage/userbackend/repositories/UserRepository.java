package com.manage.userbackend.repositories;

import com.manage.userbackend.beans.UserBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserBean, Integer> {

    UserBean findByEmail(String email);

    UserBean getById(Integer id);

    @Query("SELECT u FROM UserBean u WHERE u.role.role = 'SUPER_ADMIN'")
    UserBean findAnyAdmin();
}
