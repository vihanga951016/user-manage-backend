package com.manage.userbackend.exceptions;

public class AuthorizationException extends UserManageException {

    public AuthorizationException(Exception ex) {
        super(ex);
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Exception ex) {
        super(message, ex);
    }
}
