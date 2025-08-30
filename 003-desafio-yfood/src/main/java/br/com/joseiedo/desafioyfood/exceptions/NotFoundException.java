package br.com.joseiedo.desafioyfood.exceptions;

public class NotFoundException extends RuntimeException {
    
    public NotFoundException(String message) {
        super(message);
    }
    
    public NotFoundException(Class<?> clazz, Long id) {
        super(String.format("%s with id %d not found", clazz.getSimpleName(), id));
    }
}