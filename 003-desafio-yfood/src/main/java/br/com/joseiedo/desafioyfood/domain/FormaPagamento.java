package br.com.joseiedo.desafioyfood.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Embeddable
public class FormaPagamento {
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoPagamento tipo;
    
    @NotBlank
    @Size(max = 100)
    private String descricao;
    
    @Deprecated
    public FormaPagamento() {}
    
    public FormaPagamento(@NotNull TipoPagamento tipo, @NotBlank @Size(max = 100) String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public static Set<FormaPagamento> getFormasCompativeis(@NotNull Set<FormaPagamento> formas1, @NotNull Set<FormaPagamento> formas2) {
        Assert.notNull(formas1, "formas1 cannot be null");
        Assert.notNull(formas2, "formas2 cannot be null");
        
        Set<FormaPagamento> compativeis = new HashSet<>();
        for (FormaPagamento forma1 : formas1) {
            for (FormaPagamento forma2 : formas2) {
                if (forma1.getTipo().equals(forma2.getTipo())) {
                    compativeis.add(forma1);
                    break;
                }
            }
        }
        return compativeis;
    }
    
    public TipoPagamento getTipo() {
        return tipo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormaPagamento that = (FormaPagamento) o;
        return Objects.equals(tipo, that.tipo) && 
               Objects.equals(descricao, that.descricao);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(tipo, descricao);
    }
}