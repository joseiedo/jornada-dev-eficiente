package br.com.joseiedo.desafioyfood.domain;

public enum TipoPagamento {
    CARTAO_MASTER(true),
    CARTAO_VISA(true),
    CARTAO_ELO(true),
    CARTAO_HYPERCARD(true),
    DINHEIRO(false),
    MAQUINA(false),
    CHEQUE(false);
    
    private final boolean online;
    
    TipoPagamento(boolean online) {
        this.online = online;
    }
    
    public boolean isOnline() {
        return online;
    }
    
    public boolean isCartao() {
        return online;
    }
}