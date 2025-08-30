package br.com.joseiedo.desafioyfood.pagamentos.domain;

public enum FormaPagamento {
    VISA(TipoPagamento.CARTAO, "Visa"),
    MASTER(TipoPagamento.CARTAO, "Master"),
    ELO(TipoPagamento.CARTAO, "Elo"),
    DINHEIRO(TipoPagamento.DINHEIRO, "Dinheiro"),
    MAQUINA(TipoPagamento.MAQUINA, "Maquina"),
    CHEQUE(TipoPagamento.CHEQUE, "Cheque");

    private final TipoPagamento tipo;
    private final String descricao;

    FormaPagamento(TipoPagamento tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public TipoPagamento getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }
}