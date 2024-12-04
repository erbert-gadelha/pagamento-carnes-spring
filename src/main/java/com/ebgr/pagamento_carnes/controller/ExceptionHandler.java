package com.ebgr.pagamento_carnes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        System.err.println("handleException: " + e);
        return ResponseEntity.status(400).body("Erro: " + e.getMessage());
    }
}
