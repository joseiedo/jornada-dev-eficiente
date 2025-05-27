package com.github.joseiedo.desafiocasadocodigo.model.purchase;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

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

    public PurchaseOrder(@NonNull @Positive BigDecimal total, @NonNull @NotEmpty List<PurchaseOrderItem> items) {
        Assert.notNull(total, "Total must not be null");
        Assert.notEmpty(items, "Items must not be empty");
        this.total = total;
        this.items = items;
        Assert.isTrue(totalIsValid(), "Total is not valid for the items provided.");
    }

    @Deprecated
    public PurchaseOrder() {
    }

    public Boolean totalIsValid() {
        BigDecimal sum = items.stream()
                .map(PurchaseOrderItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.compareTo(total) == 0;
    }
}
