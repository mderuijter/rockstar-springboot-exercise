package org.mderuijter.rockstarspringbootexercise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SongNotFoundException extends RuntimeException{

    public SongNotFoundException(String msg) {
        super(msg);
    }
}
