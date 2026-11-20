package com.aluguelbicicleta.exception;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNaoEncontrado(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensagem", exception.getMessage()));
    }

    @ExceptionHandler({RestricaoException.class, DataIntegrityViolationException.class})
    public ResponseEntity<Map<String, String>> handleConflito(Exception exception) {
        String mensagem = exception instanceof RestricaoException
                ? exception.getMessage()
                : "A operação não pode ser concluída devido a uma restrição dos dados.";

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("mensagem", mensagem));
    }
}
