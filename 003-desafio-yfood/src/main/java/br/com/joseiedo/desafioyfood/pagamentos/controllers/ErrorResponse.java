package br.com.joseiedo.desafioyfood.pagamentos.controllers;

public record ErrorResponse(int status, String error, String message) {
}