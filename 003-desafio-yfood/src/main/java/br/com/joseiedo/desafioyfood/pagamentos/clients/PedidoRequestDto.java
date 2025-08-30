package br.com.joseiedo.desafioyfood.pagamentos.clients;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PedidoRequestDto(@NotNull @Positive BigDecimal valor) {
}