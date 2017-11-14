package com.alexboriskin.testapiassignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class NewExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ThereIsNoFileException.class)
    protected ResponseEntity<AwesomeException> handleThereIsNoFileException() {
        return new ResponseEntity<>(new AwesomeException("There is no such file"), HttpStatus.NOT_FOUND);
    }

    private static class AwesomeException {
        private String message;

        public AwesomeException() {
        }

        public AwesomeException(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
