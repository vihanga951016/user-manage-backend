package com.manage.userbackend.exceptions;

public class UserManageException extends Exception{

    public UserManageException() { super();}

    public UserManageException(Exception ex) { super(ex);}

    public UserManageException(String message) { super(message);}

    public UserManageException(String message, Exception ex) { super(message, ex);}
}
