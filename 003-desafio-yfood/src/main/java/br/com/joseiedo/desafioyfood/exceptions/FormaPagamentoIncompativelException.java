package br.com.joseiedo.desafioyfood.exceptions;

public class FormaPagamentoIncompativelException extends RuntimeException {
    
    public FormaPagamentoIncompativelException() {
        super("Combinacao de restaurante + usuario nao aceita a forma de pagamento selecionada");
    }
}