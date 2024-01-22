package com.manage.userbackend.repositories;

import com.manage.userbackend.beans.UserBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserBean, Integer> {

    UserBean findByEmail(String email);

    UserBean getById(Integer id);

    @Query("SELECT u FROM UserBean u WHERE u.role.role = 'SUPER_ADMIN'")
    UserBean findAnyAdmin();

    @Query("SELECT u FROM UserBean u")
    List<UserBean> getAllUsersForSuperAdmin();

    @Query("SELECT u FROM UserBean u WHERE u.role.role <> 'SUPER_ADMIN'")
    List<UserBean> getAllUsersForAdmin();

    @Query("SELECT u FROM UserBean u WHERE u.role.role = 'USER'")
    List<UserBean> getAllUsersForUser();

    @Query("UPDATE UserBean u SET u.deactivated=:status WHERE u.id=:uid")
    void userActivation(Integer uid, boolean status);
}
