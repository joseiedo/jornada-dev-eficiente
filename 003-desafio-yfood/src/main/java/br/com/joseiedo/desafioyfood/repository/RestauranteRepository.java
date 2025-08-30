package br.com.joseiedo.desafioyfood.repository;

import br.com.joseiedo.desafioyfood.domain.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
}