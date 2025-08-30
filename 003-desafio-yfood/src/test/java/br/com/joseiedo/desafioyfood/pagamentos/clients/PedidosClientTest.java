package br.com.joseiedo.desafioyfood.pagamentos.clients;

import br.com.joseiedo.desafioyfood.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {
        "server.port=8080",
        "pedidos.service.url=http://localhost:8080"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PedidosClientTest {

    @Autowired
    private PedidosClient pedidosClient;

    private final List<Long> createdPedidoIds = new ArrayList<>();

    @Test
    void deveCriarEBuscarPedido() {
        PedidoRequestDto request = new PedidoRequestDto(new BigDecimal("100.50"));
        
        ResponseEntity<PedidoResponseDto> createResponse = pedidosClient.criarPedido(request);
        
        assertEquals(201, createResponse.getStatusCode().value());
        assertNotNull(createResponse.getBody());
        assertEquals(new BigDecimal("100.50"), createResponse.getBody().valor());
        
        Long pedidoId = createResponse.getBody().id();
        createdPedidoIds.add(pedidoId);
        
        PedidoResponseDto getResponse = pedidosClient.buscarPedido(pedidoId);
        
        assertNotNull(getResponse);
        assertEquals(pedidoId, getResponse.id());
        assertEquals(new BigDecimal("100.50"), getResponse.valor());
    }

    @Test
    void deveDeletarPedido() {
        PedidoRequestDto request = new PedidoRequestDto(new BigDecimal("200.00"));
        
        ResponseEntity<PedidoResponseDto> createResponse = pedidosClient.criarPedido(request);
        Long pedidoId = createResponse.getBody().id();
        
        ResponseEntity<Void> deleteResponse = pedidosClient.deletarPedido(pedidoId);
        
        assertEquals(204, deleteResponse.getStatusCode().value());
        
        assertThrows(Exception.class, () -> pedidosClient.buscarPedido(pedidoId));
    }

    @AfterAll
    void limparPedidosCriados() {
        createdPedidoIds.forEach(id -> {
            try {
                pedidosClient.deletarPedido(id);
            } catch (Exception e) {
                // Ignora erros se o pedido já foi deletado
            }
        });
    }
}