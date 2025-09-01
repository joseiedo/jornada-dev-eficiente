package br.com.joseiedo.desafioyfood.exceptions;

public class PedidoNaoEncontradoException extends RuntimeException {
    
    public PedidoNaoEncontradoException(Long pedidoId) {
        super("Pedido with id " + pedidoId + " not found");
    }
}