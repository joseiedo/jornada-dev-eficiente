package br.com.joseiedo.desafioyfood.domain;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RegraFraudePorEmail implements RegraFraude {
    
    private static final Set<String> EMAILS_FRAUDADORES = Set.of(
        "fraudador@exemplo.com",
        "suspeito@teste.com",
        "bloqueado@email.com"
    );
    
    @Override
    public boolean ehFraude(Usuario usuario) {
        return EMAILS_FRAUDADORES.contains(usuario.getEmail());
    }
}