package com.manage.userbackend.repositories;

import com.manage.userbackend.beans.RoleBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleBean, Integer> {

    @Query("SELECT r FROM RoleBean r WHERE r.role = 'SUPER_ADMIN'")
    RoleBean getSuperAdmin();
}
