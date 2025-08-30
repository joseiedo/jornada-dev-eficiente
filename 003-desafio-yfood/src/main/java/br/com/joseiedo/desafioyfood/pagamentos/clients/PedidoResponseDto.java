package br.com.joseiedo.desafioyfood.pagamentos.clients;

import java.math.BigDecimal;

public record PedidoResponseDto(Long id, BigDecimal valor) {
}