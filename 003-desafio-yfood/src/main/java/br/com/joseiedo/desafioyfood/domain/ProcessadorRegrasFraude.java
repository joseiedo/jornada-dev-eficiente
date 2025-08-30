package br.com.joseiedo.desafioyfood.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProcessadorRegrasFraude {
    
    @Autowired
    private Set<RegraFraude> regras;
    
    public Set<FormaPagamento> aplicarRegras(Usuario usuario) {
        return usuario.getFormasPagamento(regras);
    }
}