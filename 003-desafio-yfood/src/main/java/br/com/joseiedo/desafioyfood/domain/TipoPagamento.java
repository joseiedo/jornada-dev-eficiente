package br.com.joseiedo.desafioyfood.domain;

public enum TipoPagamento {
    CARTAO(true),
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