package br.com.joseiedo.desafioyfood.pagamentos.controllers;

import br.com.joseiedo.desafioyfood.exceptions.FormaPagamentoIncompativelException;
import br.com.joseiedo.desafioyfood.exceptions.FormaPagamentoOnlineException;
import br.com.joseiedo.desafioyfood.exceptions.NotFoundException;
import br.com.joseiedo.desafioyfood.exceptions.PedidoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Resource Not Found",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlePedidoNaoEncontradoException(PedidoNaoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Pedido Not Found",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(FormaPagamentoOnlineException.class)
    public ResponseEntity<ErrorResponse> handleFormaPagamentoOnlineException(FormaPagamentoOnlineException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Payment Method",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(FormaPagamentoIncompativelException.class)
    public ResponseEntity<ErrorResponse> handleFormaPagamentoIncompativelException(FormaPagamentoIncompativelException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Incompatible Payment Method",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Validation failed");
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            message
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}