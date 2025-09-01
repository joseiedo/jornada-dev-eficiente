package br.com.joseiedo.desafioyfood.exceptions;

public class FormaPagamentoOnlineException extends RuntimeException {
    
    public FormaPagamentoOnlineException() {
        super("Forma de pagamento deve ser offline");
    }
}