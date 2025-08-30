package br.com.joseiedo.desafioyfood.pedidos.controllers;

import br.com.joseiedo.desafioyfood.pedidos.domain.Pedido;
import br.com.joseiedo.desafioyfood.pedidos.repository.PedidoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidosController {

    private final PedidoRepository pedidoRepository;

    public PedidosController(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping("/{idPedido}")
    public ResponseEntity<PedidoResponseDto> buscarPedido(@PathVariable Long idPedido) {
        Optional<Pedido> pedido = pedidoRepository.findById(idPedido);
        
        if (pedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(PedidoResponseDto.from(pedido.get()));
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDto> criarPedido(@RequestBody @Valid PedidoRequestDto request) {
        Pedido pedido = request.toPedido();
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedidoSalvo.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(PedidoResponseDto.from(pedidoSalvo));
    }

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long idPedido) {
        if (!pedidoRepository.existsById(idPedido)) {
            return ResponseEntity.notFound().build();
        }
        
        pedidoRepository.deleteById(idPedido);
        return ResponseEntity.noContent().build();
    }
}