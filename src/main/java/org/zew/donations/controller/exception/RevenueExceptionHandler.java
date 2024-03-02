package org.zew.donations.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice("org.zew.donations")
@Slf4j
public class RevenueExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RevenueAlreadyExistsException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(RevenueAlreadyExistsException e) {
        String message = getRevenueAlreadyExistsExceptionMessage();

        log.warn(message);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    public static String getRevenueAlreadyExistsExceptionMessage() {
        return "Revenue Already Exists";
    }
}