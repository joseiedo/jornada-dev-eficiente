package com.github.joseiedo.desafiocasadocodigo.model.purchase;

import com.github.joseiedo.desafiocasadocodigo.config.validators.CpfOrCnpj;
import com.github.joseiedo.desafiocasadocodigo.config.validators.NoLetters;
import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import com.github.joseiedo.desafiocasadocodigo.model.coupon.Coupon;
import com.github.joseiedo.desafiocasadocodigo.model.state.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.math.BigDecimal;

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @CpfOrCnpj
    @NotBlank
    private String document;

    @NotBlank
    private String address;

    @NotBlank
    private String complement;

    @NotBlank
    private String city;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @NotBlank
    @NoLetters
    private String phone;

    @NotNull
    @NoLetters
    private String postalCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Deprecated
    public Purchase() {
    }

    public Purchase(PurchaseBuilder purchaseBuilder) {
        this.email = purchaseBuilder.email;
        this.firstName = purchaseBuilder.firstName;
        this.lastName = purchaseBuilder.lastName;
        this.document = purchaseBuilder.document;
        this.address = purchaseBuilder.address;
        this.complement = purchaseBuilder.complement;
        this.city = purchaseBuilder.city;
        this.country = purchaseBuilder.country;
        this.state = purchaseBuilder.state;
        this.phone = purchaseBuilder.phone;
        this.postalCode = purchaseBuilder.postalCode;
        this.purchaseOrder = purchaseBuilder.purchaseOrder;
    }

    public static PurchaseBuilder builder() {
        return new PurchaseBuilder();
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", id=" + id +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDocument() {
        return document;
    }

    public String getAddress() {
        return address;
    }

    public String getComplement() {
        return complement;
    }

    public String getCity() {
        return city;
    }

    public Country getCountry() {
        return country;
    }

    public State getState() {
        return state;
    }

    public String getPhone() {
        return phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        Assert.notNull(coupon, "Coupon must not be null");
        Assert.isTrue(!coupon.isExpired(), "Coupon is expired");
        this.coupon = coupon;
    }

    public Boolean hasCoupon() {
        return coupon != null;
    }

    public BigDecimal getTotalWithDiscount() {
        if (coupon == null) {
            return null;
        }
        return coupon.applyDiscount(purchaseOrder);
    }

    public static class PurchaseBuilder {
        private String email;
        private String firstName;
        private String lastName;
        private String document;
        private String address;
        private String complement;
        private String city;
        private Country country;
        private State state;
        private String phone;
        private String postalCode;
        private PurchaseOrder purchaseOrder;

        public PurchaseBuilder email(@Email @NotBlank String email) {
            this.email = email;
            return this;
        }

        public PurchaseBuilder firstName(@NotBlank String firstName) {
            this.firstName = firstName;
            return this;
        }

        public PurchaseBuilder lastName(@NotBlank String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PurchaseBuilder document(@NotBlank String document) {
            this.document = document;
            return this;
        }

        public PurchaseBuilder address(@NotBlank String address) {
            this.address = address;
            return this;
        }

        public PurchaseBuilder complement(@NotBlank String complement) {
            this.complement = complement;
            return this;
        }

        public PurchaseBuilder city(@NotBlank String city) {
            this.city = city;
            return this;
        }

        public PurchaseBuilder country(@NonNull Country country) {
            this.country = country;
            return this;
        }

        public PurchaseBuilder state(State state) {
            this.state = state;
            return this;
        }

        public PurchaseBuilder phone(@NotBlank String phone) {
            this.phone = phone;
            return this;
        }

        public PurchaseBuilder postalCode(@NonNull String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public PurchaseBuilder order(@NonNull PurchaseOrder purchaseOrder) {
            this.purchaseOrder = purchaseOrder;
            return this;
        }

        public Purchase build() {
            return new Purchase(this);
        }
    }
}
