package com.github.joseiedo.desafiocasadocodigo.model.state;

import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
public class State {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Deprecated
    public State() {
    }

    public State(@NotBlank String name, @NotNull Country country) {
        this.name = name;
        this.country = country;
        this.country.getStates().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(name, state.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public Long getId() {
        return id;
    }
}
