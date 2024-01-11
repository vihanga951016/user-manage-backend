package com.manage.userbackend.repositories;

import com.manage.userbackend.beans.UserPermissionBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermissionBean, Integer> {
}
