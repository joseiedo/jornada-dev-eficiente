package br.com.joseiedo.desafioyfood.pedidos.controllers;

import br.com.joseiedo.desafioyfood.pedidos.domain.Pedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PedidoRequestDto(@NotNull @Positive BigDecimal valor) {
    
    public Pedido toPedido() {
        return new Pedido(valor);
    }
}