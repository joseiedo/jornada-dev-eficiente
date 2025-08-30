package br.com.joseiedo.desafioyfood.pedidos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal valor;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormaPagamento formaPagamento;
    
    @NotNull
    @Column(nullable = false)
    private Long restauranteId;
    
    @NotNull
    @Column(nullable = false)
    private Long usuarioId;
    
    @Deprecated
    public Pedido() {}
    
    public Pedido(@NotNull @Positive BigDecimal valor, @NotNull FormaPagamento formaPagamento, 
                  @NotNull Long restauranteId, @NotNull Long usuarioId) {
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.restauranteId = restauranteId;
        this.usuarioId = usuarioId;
    }

    public Long getId() {
        return id;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }
    
    public Long getRestauranteId() {
        return restauranteId;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}