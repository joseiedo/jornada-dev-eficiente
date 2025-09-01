package br.com.joseiedo.desafioyfood.pagamentos.controllers;

import br.com.joseiedo.desafioyfood.pagamentos.clients.PedidoResponseDto;
import br.com.joseiedo.desafioyfood.pagamentos.clients.PedidosClient;
import br.com.joseiedo.desafioyfood.pagamentos.domain.*;
import br.com.joseiedo.desafioyfood.pagamentos.repository.RestauranteRepository;
import br.com.joseiedo.desafioyfood.pagamentos.repository.TransacaoRepository;
import br.com.joseiedo.desafioyfood.pagamentos.repository.UsuarioRepository;
import br.com.joseiedo.desafioyfood.pedidos.domain.Pedido;
import br.com.joseiedo.desafioyfood.pedidos.repository.PedidoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @MockitoBean
    private PedidosClient pedidosClient;

    @BeforeEach
    void setup() {
        // Mock para pedidos que existem (retorna dados válidos)
        when(pedidosClient.buscarPedido(any(Long.class)))
            .thenAnswer(invocation -> {
                Long pedidoId = invocation.getArgument(0);
                if (pedidoId.equals(999L)) {
                    throw new FeignException.NotFound("Pedido not found", 
                        feign.Request.create(feign.Request.HttpMethod.GET, "url", java.util.Collections.emptyMap(), null, null, null),
                        null, null);
                }
                return new PedidoResponseDto(pedidoId, BigDecimal.valueOf(50.00));
            });
    }

    @AfterEach
    void cleanup() {
        transacaoRepository.deleteAll();
        restauranteRepository.deleteAll();
        usuarioRepository.deleteAll();
        pedidoRepository.deleteAll();
    }

    @Test
    void deveRetornarTransacaoComStatusEsperandoPagamentoQuandoCriarTransacaoOfflineValida() throws Exception {
        Usuario usuario = UsuarioFactory.createWithFormaDePagamentoDinheiro();
        usuario = usuarioRepository.save(usuario);

        Restaurante restaurante = RestauranteFactory.createAceitandoDinheiro();
        restaurante = restauranteRepository.save(restaurante);

        Pedido pedido = new Pedido(BigDecimal.valueOf(50.00), br.com.joseiedo.desafioyfood.pedidos.domain.FormaPagamento.DINHEIRO, 
                                  restaurante.getId(), usuario.getId());
        pedido = pedidoRepository.save(pedido);

        TransacaoRequestDto request = new TransacaoRequestDto(
            pedido.getId(),
            restaurante.getId(),
            usuario.getId(),
            BigDecimal.valueOf(50.00),
            FormaPagamento.DINHEIRO,
            null
        );

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pedidoId").value(pedido.getId()))
                .andExpect(jsonPath("$.usuarioId").value(usuario.getId()))
                .andExpect(jsonPath("$.valor").value(50.00))
                .andExpect(jsonPath("$.status").value("ESPERANDO_PAGAMENTO"))
                .andExpect(jsonPath("$.formaPagamento").value("DINHEIRO"))
                .andExpect(jsonPath("$.informacoesExtras").isEmpty())
                .andExpect(jsonPath("$.instanteCriacao").exists())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void deveRetornarTransacaoComInformacoesExtrasQuandoFormaPagamentoMaquina() throws Exception {
        Usuario usuario = UsuarioFactory.createWithFormaDePagamentoMaquina();
        usuario = usuarioRepository.save(usuario);

        Restaurante restaurante = RestauranteFactory.createAceitandoMaquina();
        restaurante = restauranteRepository.save(restaurante);

        Pedido pedido = new Pedido(BigDecimal.valueOf(25.50), br.com.joseiedo.desafioyfood.pedidos.domain.FormaPagamento.MAQUINA,
                                  restaurante.getId(), usuario.getId());
        pedido = pedidoRepository.save(pedido);

        TransacaoRequestDto request = new TransacaoRequestDto(
            pedido.getId(),
            restaurante.getId(),
            usuario.getId(),
            BigDecimal.valueOf(25.50),
            FormaPagamento.MAQUINA,
            "Terminal POS 001"
        );

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formaPagamento").value("MAQUINA"))
                .andExpect(jsonPath("$.informacoesExtras").value("Terminal POS 001"))
                .andExpect(jsonPath("$.status").value("ESPERANDO_PAGAMENTO"));
    }

    @Test
    void deveRetornarBadRequestQuandoFormaPagamentoForOnline() throws Exception {
        TransacaoRequestDto request = TransacaoFactory.createInvalidOnlineTransactionRequest();

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value("Forma de pagamento deve ser offline"));
    }

    @Test
    void deveRetornarBadRequestQuandoPedidoNaoExiste() throws Exception {
        TransacaoRequestDto request = TransacaoFactory.createTransactionWithNonExistentPedido();

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test
    void deveRetornarBadRequestQuandoRestauranteNaoAceitaFormaPagamento() throws Exception {
        Usuario usuario = UsuarioFactory.createWithMultiplasFormasPagamento();
        usuario = usuarioRepository.save(usuario);

        Restaurante restaurante = RestauranteFactory.createAceitandoCartoes();
        restaurante = restauranteRepository.save(restaurante);

        Pedido pedido = new Pedido(BigDecimal.valueOf(50.00), br.com.joseiedo.desafioyfood.pedidos.domain.FormaPagamento.VISA,
                                  restaurante.getId(), usuario.getId());
        pedido = pedidoRepository.save(pedido);

        TransacaoRequestDto request = new TransacaoRequestDto(
            pedido.getId(),
            restaurante.getId(),
            usuario.getId(),
            BigDecimal.valueOf(50.00),
            FormaPagamento.DINHEIRO, // Restaurante não aceita dinheiro
            null
        );

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value("Combinacao de restaurante + usuario nao aceita a forma de pagamento selecionada"));
    }

    @Test
    void deveRetornarBadRequestQuandoCamposObrigatoriosForemNulos() throws Exception {
        TransacaoRequestDto request = new TransacaoRequestDto(
            null, // pedidoId nulo
            null, // restauranteId nulo
            null, // usuarioId nulo
            null, // valor nulo
            null, // formaPagamento nulo
            null
        );

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test
    void deveRetornarBadRequestQuandoValorForNegativo() throws Exception {
        TransacaoRequestDto request = new TransacaoRequestDto(
            1L,
            1L,
            1L,
            BigDecimal.valueOf(-10.00), // valor negativo
            FormaPagamento.DINHEIRO,
            null
        );

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test
    void deveListarTodasTransacoesQuandoChamarEndpointGet() throws Exception {
        Transacao transacao1 = TransacaoFactory.createSavedTransaction();
        Transacao transacao2 = TransacaoFactory.createSavedTransactionWithMaquina();
        
        transacaoRepository.save(transacao1);
        transacaoRepository.save(transacao2);

        mockMvc.perform(get("/transacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].pedidoId").exists())
                .andExpect(jsonPath("$[0].status").value("ESPERANDO_PAGAMENTO"))
                .andExpect(jsonPath("$[1].pedidoId").exists())
                .andExpect(jsonPath("$[1].status").value("ESPERANDO_PAGAMENTO"));
    }

    @Test
    void deveRetornarArrayVazioQuandoNaoHouverTransacoes() throws Exception {
        mockMvc.perform(get("/transacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}