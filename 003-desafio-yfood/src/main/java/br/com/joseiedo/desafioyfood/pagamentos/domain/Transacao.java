package br.com.joseiedo.desafioyfood.pagamentos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Transacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(nullable = false)
    private Long pedidoId;
    
    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal valor;
    
    @NotNull
    @Column(nullable = false)
    private Long usuarioId;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTransacao status;
    
    @NotNull
    @Column(nullable = false)
    private LocalDateTime instanteCriacao;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormaPagamento formaPagamento;
    
    @Column
    private String informacoesExtras;
    
    @Deprecated
    public Transacao() {}
    
    public Transacao(@NotNull Long pedidoId, 
                    @NotNull @Positive BigDecimal valor, 
                    @NotNull Long usuarioId, 
                    @NotNull StatusTransacao status, 
                    @NotNull FormaPagamento formaPagamento,
                    String informacoesExtras) {
        this.pedidoId = pedidoId;
        this.valor = valor;
        this.usuarioId = usuarioId;
        this.status = status;
        this.formaPagamento = formaPagamento;
        this.informacoesExtras = informacoesExtras;
        this.instanteCriacao = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public Long getPedidoId() {
        return pedidoId;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public StatusTransacao getStatus() {
        return status;
    }
    
    public LocalDateTime getInstanteCriacao() {
        return instanteCriacao;
    }
    
    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }
    
    public String getInformacoesExtras() {
        return informacoesExtras;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transacao transacao = (Transacao) o;
        return Objects.equals(id, transacao.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}