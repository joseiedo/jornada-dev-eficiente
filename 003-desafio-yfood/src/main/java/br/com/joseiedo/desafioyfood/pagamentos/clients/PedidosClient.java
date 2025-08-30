package br.com.joseiedo.desafioyfood.pagamentos.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "pedidos", url = "${pedidos.service.url:http://localhost:8080}")
public interface PedidosClient {

    @GetMapping("/api/pedidos/{idPedido}")
    PedidoResponseDto buscarPedido(@PathVariable Long idPedido);

    @PostMapping("/api/pedidos")
    ResponseEntity<PedidoResponseDto> criarPedido(@RequestBody PedidoRequestDto request);

    @DeleteMapping("/api/pedidos/{idPedido}")
    ResponseEntity<Void> deletarPedido(@PathVariable Long idPedido);
}