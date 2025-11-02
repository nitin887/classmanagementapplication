package com.classmanagementsystem.classmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TeacherAlreadyExistException extends RuntimeException{
    public TeacherAlreadyExistException(String message){
        super(message);
    }
}
