package br.com.joseiedo.desafioyfood.pedidos.repository;

import br.com.joseiedo.desafioyfood.pedidos.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}