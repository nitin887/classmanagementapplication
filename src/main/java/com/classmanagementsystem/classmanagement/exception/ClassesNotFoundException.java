package com.classmanagementsystem.classmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClassesNotFoundException extends RuntimeException{
    public ClassesNotFoundException(String message){
        super(message);
    }
}
