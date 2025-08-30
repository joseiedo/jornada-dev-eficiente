package br.com.joseiedo.desafioyfood.pedidos.controllers;

import br.com.joseiedo.desafioyfood.pedidos.domain.FormaPagamento;
import br.com.joseiedo.desafioyfood.pedidos.domain.Pedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PedidoRequestDto(@NotNull @Positive BigDecimal valor, @NotNull FormaPagamento formaPagamento, 
                               @NotNull Long restauranteId, @NotNull Long usuarioId) {
    
    public Pedido toPedido() {
        return new Pedido(valor, formaPagamento, restauranteId, usuarioId);
    }
}