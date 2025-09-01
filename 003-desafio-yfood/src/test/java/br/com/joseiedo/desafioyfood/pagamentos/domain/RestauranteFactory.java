package br.com.joseiedo.desafioyfood.pagamentos.domain;

import java.lang.reflect.Field;
import java.util.Set;

public class RestauranteFactory {
    
    public static Restaurante createWithOfflinePayments(Long id) {
        Restaurante restaurante = new Restaurante("Restaurante Offline", 
            Set.of(FormaPagamento.DINHEIRO, FormaPagamento.MAQUINA, FormaPagamento.CHEQUE));
        return setId(restaurante, id);
    }
    
    public static Restaurante createWithOnlinePayments(Long id) {
        Restaurante restaurante = new Restaurante("Restaurante Online", 
            Set.of(FormaPagamento.VISA, FormaPagamento.MASTER));
        return setId(restaurante, id);
    }
    
    public static Restaurante createAceitando(FormaPagamento formaPagamento) {
        return new Restaurante("Restaurante " + formaPagamento.name(), Set.of(formaPagamento));
    }
    
    public static Restaurante createAceitandoApenasVisa() {
        return createAceitando(FormaPagamento.VISA);
    }
    
    public static Restaurante createAceitandoDinheiro() {
        return createAceitando(FormaPagamento.DINHEIRO);
    }
    
    public static Restaurante createAceitandoCartoes() {
        return new Restaurante("Restaurante Cartões", 
            Set.of(FormaPagamento.VISA, FormaPagamento.MASTER, FormaPagamento.ELO));
    }
    
    public static Restaurante createAceitandoTodasFormas() {
        return new Restaurante("Restaurante Todas Formas", Set.of(FormaPagamento.values()));
    }
    
    public static Restaurante createAceitandoMaquina() {
        return createAceitando(FormaPagamento.MAQUINA);
    }
    
    private static Restaurante setId(Restaurante restaurante, Long id) {
        try {
            Field field = Restaurante.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(restaurante, id);
            return restaurante;
        } catch (Exception e) {
            throw new RuntimeException("Failed to set ID", e);
        }
    }
}