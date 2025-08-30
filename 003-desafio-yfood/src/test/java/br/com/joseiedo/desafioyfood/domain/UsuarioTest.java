package br.com.joseiedo.desafioyfood.domain;

import br.com.joseiedo.desafioyfood.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UsuarioTest {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @AfterAll
    static void clearDatabase(@Autowired UsuarioRepository usuarioRepository) {
        usuarioRepository.deleteAll();
    }

    @Test
    void deveLancarExcecaoQuandoFormasPagamentoEstiverVazio() {
        Set<FormaPagamento> formasVazias = new HashSet<>();
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Usuario("usuario@test.com", formasVazias);
        });
    }

    @Test
    void devePersistirUsuarioValidoComSucesso() {
        FormaPagamento visa = FormaPagamento.VISA;
        Set<FormaPagamento> formas = Set.of(visa);
        Usuario usuario = new Usuario("usuario@test.com", formas);
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        
        assertNotNull(usuarioSalvo);
        assertNotNull(usuarioSalvo.getId());
        assertEquals("usuario@test.com", usuarioSalvo.getEmail());
        assertEquals(1, usuarioSalvo.getFormasPagamento().size());
    }

    @Test
    void deveFalharValidacaoComEmailInvalido() {
        FormaPagamento visa = FormaPagamento.VISA;
        
        assertThrows(Exception.class, () -> {
            Usuario usuario = new Usuario("email-invalido", Set.of(visa));
            usuarioRepository.saveAndFlush(usuario);
        });
    }

    @Test
    void deveFalharValidacaoComEmailVazio() {
        FormaPagamento visa = FormaPagamento.VISA;
        
        assertThrows(Exception.class, () -> {
            Usuario usuario = new Usuario("", Set.of(visa));
            usuarioRepository.saveAndFlush(usuario);
        });
    }

    @Test
    void deveRetornarApenasFormasOfflineQuandoUsuarioEhFraudador() {
        Set<FormaPagamento> todasFormas = Set.of(
            FormaPagamento.VISA,
            FormaPagamento.MASTER, 
            FormaPagamento.DINHEIRO,
            FormaPagamento.CHEQUE
        );
        
        Usuario usuario = new Usuario("fraudador@exemplo.com", todasFormas);
        
        RegraFraude regraFraude = new RegraFraude() {
            @Override
            public boolean ehFraude(Usuario usuario) {
                return true;
            }
        };
        
        Set<RegraFraude> regras = Set.of(regraFraude);
        
        Set<FormaPagamento> formasFiltradas = usuario.getFormasPagamentoFiltrandoPorRegras(regras);
        
        assertEquals(2, formasFiltradas.size());
        assertTrue(formasFiltradas.contains(FormaPagamento.DINHEIRO));
        assertTrue(formasFiltradas.contains(FormaPagamento.CHEQUE));
        assertFalse(formasFiltradas.contains(FormaPagamento.VISA));
        assertFalse(formasFiltradas.contains(FormaPagamento.MASTER));
    }
}