package br.com.joseiedo.desafioyfood.controllers;

public record ErrorResponse(int status, String error, String message) {
}