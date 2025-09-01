package br.com.joseiedo.desafioyfood.pagamentos.clients;

import br.com.joseiedo.desafioyfood.pedidos.domain.FormaPagamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PedidoRequestDto(
        @NotNull @Positive BigDecimal valor,
        @NotNull FormaPagamento formaPagamento,
        @NotNull Long restauranteId,
        @NotNull Long usuarioId
) {
}