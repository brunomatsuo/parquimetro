package com.parquimetro.fiap.exception.controller;

import com.parquimetro.fiap.exception.service.ControllerNotFoundException;
import com.parquimetro.fiap.exception.service.DatabaseException;
import com.parquimetro.fiap.exception.service.DefaultError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {
    private DefaultError error = new DefaultError();

    @ExceptionHandler(ControllerNotFoundException.class)
    public ResponseEntity<DefaultError> entityNotFound(ControllerNotFoundException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(status.value());
        error.setError("Entidade não encontrada");
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(this.error);
    }


    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<DefaultError> entityNotFound(DatabaseException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(status.value());
        error.setError("Database error");
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(this.error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidacaoForm> validation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidacaoForm validacaoForm = new ValidacaoForm();
        validacaoForm.setTimestamp(LocalDateTime.now());
        validacaoForm.setStatus(status.value());
        validacaoForm.setError("Erro de  validação");
        validacaoForm.setMessage(exception.getMessage());
        validacaoForm.setPath(request.getRequestURI());

        for (FieldError field: exception.getBindingResult().getFieldErrors()) {
            validacaoForm.addMenssagens(field.getField(), field.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(validacaoForm);
    }


}