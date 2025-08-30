package br.com.joseiedo.desafioyfood.pagamentos.domain;

public interface RegraFraude {
    boolean ehFraude(Usuario usuario);
}