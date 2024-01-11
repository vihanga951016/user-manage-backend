package com.manage.userbackend.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_authentication")
public class UserAuthenticationBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date loginTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logoutTime;
}
