package com.manage.userbackend.repositories;

import com.manage.userbackend.beans.PermissionsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionsBean, Integer> {
}
