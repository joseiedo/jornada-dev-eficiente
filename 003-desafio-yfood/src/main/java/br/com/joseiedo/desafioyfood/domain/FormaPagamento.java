package br.com.joseiedo.desafioyfood.domain;

import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum FormaPagamento {
    VISA(TipoPagamento.CARTAO, "Visa"),
    MASTER(TipoPagamento.CARTAO, "Master"),
    ELO(TipoPagamento.CARTAO, "Elo"),
    DINHEIRO(TipoPagamento.DINHEIRO, "Dinheiro"),
    MAQUINA(TipoPagamento.MAQUINA, "Máquina"),
    CHEQUE(TipoPagamento.CHEQUE, "Cheque");

    private final TipoPagamento tipo;
    private final String descricao;

    FormaPagamento(TipoPagamento tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public static Set<FormaPagamento> getFormasCompativeis(@NotNull Set<FormaPagamento> formas1, @NotNull Set<FormaPagamento> formas2) {
        Assert.notNull(formas1, "formas1 cannot be null");
        Assert.notNull(formas2, "formas2 cannot be null");

        return formas1.stream().filter(formas2::contains).collect(Collectors.toSet());
    }

    public TipoPagamento getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }
}