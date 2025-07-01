package com.github.joseiedo.desafiocasadocodigo.model.country;

import com.github.joseiedo.desafiocasadocodigo.model.state.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private Set<State> states = new java.util.HashSet<>();

    @Deprecated
    public Country() {
    }

    public Country(@NotBlank String name) {
        this.name = name.trim().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Set<State> getStates() {
        return states;
    }

    public Long getId() {
        return id;
    }

    public Boolean containsState(State state) {
        return states.contains(state);
    }

    public Boolean hasStates() {
        return !states.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(name, country.name) && Objects.equals(states, country.states);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, states);
    }
}
