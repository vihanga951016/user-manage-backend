package com.manage.userbackend.beans.requests.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestBean {
    private String message;
    private boolean hasError;
    private Integer accessId;
}
