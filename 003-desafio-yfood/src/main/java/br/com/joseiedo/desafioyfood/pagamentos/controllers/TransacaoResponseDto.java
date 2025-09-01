package br.com.joseiedo.desafioyfood.pagamentos.controllers;

import br.com.joseiedo.desafioyfood.pagamentos.domain.FormaPagamento;
import br.com.joseiedo.desafioyfood.pagamentos.domain.StatusTransacao;
import br.com.joseiedo.desafioyfood.pagamentos.domain.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponseDto(
        Long id,
        Long pedidoId,
        Long usuarioId,
        BigDecimal valor,
        StatusTransacao status,
        LocalDateTime instanteCriacao,
        FormaPagamento formaPagamento,
        String informacoesExtras
) {
    
    public TransacaoResponseDto(Transacao transacao) {
        this(
            transacao.getId(),
            transacao.getPedidoId(),
            transacao.getUsuarioId(),
            transacao.getValor(),
            transacao.getStatus(),
            transacao.getInstanteCriacao(),
            transacao.getFormaPagamento(),
            transacao.getInformacoesExtras()
        );
    }
}