package br.com.joseiedo.desafioyfood.pagamentos.controllers;

import br.com.joseiedo.desafioyfood.pagamentos.domain.FormaPagamento;
import br.com.joseiedo.desafioyfood.pagamentos.domain.StatusTransacao;
import br.com.joseiedo.desafioyfood.pagamentos.domain.Transacao;
import br.com.joseiedo.desafioyfood.pagamentos.validators.ValidCompatiblePayment;
import br.com.joseiedo.desafioyfood.pagamentos.validators.ValidExistingPedido;
import br.com.joseiedo.desafioyfood.pagamentos.validators.ValidOfflinePayment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@ValidCompatiblePayment
public record TransacaoRequestDto(
        @NotNull @ValidExistingPedido Long pedidoId,
        @NotNull Long restauranteId,
        @NotNull Long usuarioId,
        @NotNull @Positive BigDecimal valor,
        @NotNull @ValidOfflinePayment FormaPagamento formaPagamento,
        String informacoesExtras
) {
    
    public Transacao toEntity() {
        return new Transacao(
            pedidoId,
            valor,
            usuarioId,
            StatusTransacao.ESPERANDO_PAGAMENTO,
            formaPagamento,
            informacoesExtras
        );
    }
}