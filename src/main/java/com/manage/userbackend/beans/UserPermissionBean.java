package com.manage.userbackend.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_permissions")
public class UserPermissionBean {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "roleId")
    private RoleBean role;
    @ManyToOne
    @JoinColumn(name = "permissionId")
    private PermissionsBean permission;
}
