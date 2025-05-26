package com.github.joseiedo.desafiocasadocodigo.model.purchase;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin("1.00")
    private BigDecimal total;

    @NotNull
    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    private List<PurchaseOrderItem> items;

    public PurchaseOrder(BigDecimal total, List<PurchaseOrderItem> items) {
        this.total = total;
        this.items = items;
    }

    @Deprecated
    public PurchaseOrder() {
    }
}
