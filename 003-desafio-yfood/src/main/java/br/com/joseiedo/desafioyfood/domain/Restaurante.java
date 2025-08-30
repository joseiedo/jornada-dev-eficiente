package br.com.joseiedo.desafioyfood.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Restaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String nome;
    
    @NotEmpty
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "restaurante_formas_pagamento")
    private Set<FormaPagamento> formasPagamentoAceitas = new HashSet<>();
    
    @Deprecated
    public Restaurante() {}
    
    public Restaurante(@NotBlank String nome, @NotEmpty Set<FormaPagamento> formasPagamentoAceitas) {
        this.nome = nome;
        this.formasPagamentoAceitas = new HashSet<>(formasPagamentoAceitas);
    }
    
    public Set<FormaPagamento> getFormasCompativeisCom(Set<FormaPagamento> formasUsuario) {
        return FormaPagamento.getFormasCompativeis(formasPagamentoAceitas, formasUsuario);
    }
    
    public Long getId() {
        return id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurante that = (Restaurante) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}