package com.classmanagementsystem.classmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ClassesAlreadyExistException extends RuntimeException {
    public ClassesAlreadyExistException(String m){
        super(m);
    }
}
