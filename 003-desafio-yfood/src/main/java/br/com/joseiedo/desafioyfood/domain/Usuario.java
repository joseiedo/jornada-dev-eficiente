package br.com.joseiedo.desafioyfood.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;
    
    @NotEmpty
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "usuario_formas_pagamento")
    @Size(min=1)
    private Set<FormaPagamento> formasPagamento = new HashSet<>();
    
    @Deprecated
    public Usuario() {}
    
    public Usuario(@NotBlank @Email String email, @NotEmpty Set<FormaPagamento> formasPagamento) {
        this.email = email;
        Assert.isTrue(formasPagamento.size() == 1, "formasPagamento é menor que 1");
        this.formasPagamento = new HashSet<>(formasPagamento);
    }
    
    public Long getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public Set<FormaPagamento> getFormasPagamento() {
        return new HashSet<>(formasPagamento);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}