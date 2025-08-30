package br.com.joseiedo.desafioyfood.pagamentos.controllers;

import br.com.joseiedo.desafioyfood.pagamentos.domain.RestauranteFactory;
import br.com.joseiedo.desafioyfood.pagamentos.domain.UsuarioFactory;
import br.com.joseiedo.desafioyfood.pagamentos.domain.Restaurante;
import br.com.joseiedo.desafioyfood.pagamentos.domain.Usuario;
import br.com.joseiedo.desafioyfood.pagamentos.repository.RestauranteRepository;
import br.com.joseiedo.desafioyfood.pagamentos.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RestauranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @AfterAll
    static void clearDatabase(@Autowired RestauranteRepository restauranteRepository, 
                              @Autowired UsuarioRepository usuarioRepository) {
        restauranteRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void deveRetornarVisaQuandoUsuarioERestauranteAceitamVisa() throws Exception {
        Usuario usuario = UsuarioFactory.createWithFormaDePagamentoCartao();
        usuario = usuarioRepository.save(usuario);

        Restaurante restaurante = RestauranteFactory.createAceitandoApenasVisa();
        restaurante = restauranteRepository.save(restaurante);

        mockMvc.perform(get("/restaurantes/{restauranteId}/formas-pagamento-compativeis/{usuarioId}", 
                        restaurante.getId(), usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("VISA"))
                .andExpect(jsonPath("$[0].descricao").value("Visa"));
    }

    @Test
    void deveRetornarDinheiroQuandoUsuarioERestauranteAceitamDinheiro() throws Exception {
        Usuario usuario = UsuarioFactory.createWithFormaDePagamentoDinheiro();
        usuario = usuarioRepository.save(usuario);

        Restaurante restaurante = RestauranteFactory.createAceitandoDinheiro();
        restaurante = restauranteRepository.save(restaurante);

        mockMvc.perform(get("/restaurantes/{restauranteId}/formas-pagamento-compativeis/{usuarioId}", 
                        restaurante.getId(), usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("DINHEIRO"))
                .andExpect(jsonPath("$[0].descricao").value("Dinheiro"));
    }

    @Test
    void deveRetornarCartoesCompativeisQuandoUsuarioTemMultiplasFormasERestauranteAceitaCartoes() throws Exception {
        Usuario usuario = UsuarioFactory.createWithMultiplasFormasPagamento();
        usuario = usuarioRepository.save(usuario);

        Restaurante restaurante = RestauranteFactory.createAceitandoCartoes();
        restaurante = restauranteRepository.save(restaurante);

        mockMvc.perform(get("/restaurantes/{restauranteId}/formas-pagamento-compativeis/{usuarioId}", 
                        restaurante.getId(), usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.id == 'VISA')]").exists())
                .andExpect(jsonPath("$[?(@.id == 'MASTER')]").exists());
    }

    @Test
    void deveRetornarArrayVazioQuandoNaoHaFormasCompativeis() throws Exception {
        Usuario usuario = UsuarioFactory.createWithFormaDePagamentoDinheiro();
        usuario = usuarioRepository.save(usuario);

        Restaurante restaurante = RestauranteFactory.createAceitandoCartoes();
        restaurante = restauranteRepository.save(restaurante);

        mockMvc.perform(get("/restaurantes/{restauranteId}/formas-pagamento-compativeis/{usuarioId}", 
                        restaurante.getId(), usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void deveRetornarVisaEMasterQuandoUsuarioTemVisaMasterERestauranteEhCompleto() throws Exception {
        Usuario usuario = UsuarioFactory.createWithVisaEMaster();
        usuario = usuarioRepository.save(usuario);

        Restaurante restaurante = RestauranteFactory.createAceitandoTodasFormas();
        restaurante = restauranteRepository.save(restaurante);

        mockMvc.perform(get("/restaurantes/{restauranteId}/formas-pagamento-compativeis/{usuarioId}", 
                        restaurante.getId(), usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.id == 'VISA')]").exists())
                .andExpect(jsonPath("$[?(@.id == 'MASTER')]").exists());
    }

    @Test
    void deveRetornar404QuandoRestauranteNaoExiste() throws Exception {
        Usuario usuario = UsuarioFactory.createWithFormaDePagamentoCartao();
        usuario = usuarioRepository.save(usuario);

        mockMvc.perform(get("/restaurantes/{restauranteId}/formas-pagamento-compativeis/{usuarioId}", 
                        999L, usuario.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                .andExpect(jsonPath("$.message").value("Restaurante with id 999 not found"));
    }

    @Test
    void deveRetornar404QuandoUsuarioNaoExiste() throws Exception {
        Restaurante restaurante = RestauranteFactory.createAceitandoApenasVisa();
        restaurante = restauranteRepository.save(restaurante);

        mockMvc.perform(get("/restaurantes/{restauranteId}/formas-pagamento-compativeis/{usuarioId}", 
                        restaurante.getId(), 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                .andExpect(jsonPath("$.message").value("Usuario with id 999 not found"));
    }

    @Test
    void deveRetornar404ComErroDeRestauranteQuandoAmbosNaoExistem() throws Exception {
        mockMvc.perform(get("/restaurantes/{restauranteId}/formas-pagamento-compativeis/{usuarioId}", 
                        999L, 888L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                .andExpect(jsonPath("$.message").value("Restaurante with id 999 not found"));
    }

}