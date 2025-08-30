package br.com.joseiedo.desafioyfood.pagamentos.repository;

import br.com.joseiedo.desafioyfood.pagamentos.domain.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
}