package com.manage.userbackend.repositories;

import com.manage.userbackend.beans.UserPermissionBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermissionBean, Integer> {

    @Query("SELECT up FROM UserPermissionBean up WHERE up.role.id=:roleId AND up.permission.id=:permissionId")
    UserPermissionBean getUserPermissionsByRoleAndPermission(Integer roleId, Integer permissionId);
}
