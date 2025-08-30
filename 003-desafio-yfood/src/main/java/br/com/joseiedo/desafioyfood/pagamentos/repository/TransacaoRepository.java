package br.com.joseiedo.desafioyfood.pagamentos.repository;

import br.com.joseiedo.desafioyfood.pagamentos.domain.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    
    List<Transacao> findByUsuarioId(Long usuarioId);
    
    List<Transacao> findByPedidoId(Long pedidoId);
}