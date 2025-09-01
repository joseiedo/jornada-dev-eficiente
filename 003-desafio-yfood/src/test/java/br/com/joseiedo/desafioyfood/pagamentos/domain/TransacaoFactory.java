package br.com.joseiedo.desafioyfood.pagamentos.domain;

import br.com.joseiedo.desafioyfood.pagamentos.controllers.TransacaoRequestDto;

import java.math.BigDecimal;

public class TransacaoFactory {
    
    
    public static TransacaoRequestDto createInvalidOnlineTransactionRequest() {
        return new TransacaoRequestDto(
            1L, // pedidoId
            1L, // restauranteId
            1L, // usuarioId
            BigDecimal.valueOf(50.00),
            FormaPagamento.VISA,
            null
        );
    }
    
    public static TransacaoRequestDto createTransactionWithNonExistentPedido() {
        return new TransacaoRequestDto(
            999L, // pedidoId não existe
            1L, // restauranteId
            1L, // usuarioId
            BigDecimal.valueOf(50.00),
            FormaPagamento.DINHEIRO,
            null
        );
    }
    
    
    public static Transacao createSavedTransaction(FormaPagamento formaPagamento, String extraInfo) {
        Long pedidoId = formaPagamento == FormaPagamento.DINHEIRO ? 1L : 2L;
        BigDecimal valor = formaPagamento == FormaPagamento.DINHEIRO ? BigDecimal.valueOf(50.00) : BigDecimal.valueOf(75.25);
        Long usuarioId = formaPagamento == FormaPagamento.DINHEIRO ? 1L : 2L;
        
        return new Transacao(
            pedidoId,
            valor,
            usuarioId,
            StatusTransacao.ESPERANDO_PAGAMENTO,
            formaPagamento,
            extraInfo
        );
    }
    
    public static Transacao createSavedTransaction() {
        return createSavedTransaction(FormaPagamento.DINHEIRO, null);
    }
    
    public static Transacao createSavedTransactionWithMaquina() {
        return createSavedTransaction(FormaPagamento.MAQUINA, "Terminal POS");
    }
}