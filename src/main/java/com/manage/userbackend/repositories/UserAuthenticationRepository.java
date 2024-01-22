package com.manage.userbackend.repositories;

import com.manage.userbackend.beans.UserAuthenticationBean;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthenticationBean, Integer> {

    @Query("SELECT ua.id FROM UserAuthenticationBean ua WHERE ua.username=:email AND ua.logoutTime is null")
    List<Integer> getAllLoggedInEntityIds(String email);

    @Query("SELECT ua.id FROM UserAuthenticationBean ua WHERE ua.username=:email AND ua.logoutTime is null")
    Integer getLoggedInEntityId(String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserAuthenticationBean ua SET ua.logoutTime=:date WHERE ua.id in (:ids)")
    void updateAllLoggedInEntities(List<Integer> ids, Date date);

    @Modifying
    @Transactional
    @Query("UPDATE UserAuthenticationBean ua SET ua.logoutTime=:date WHERE ua.id=:id")
    void updateLoggedInEntity(Integer id, Date date);
}
