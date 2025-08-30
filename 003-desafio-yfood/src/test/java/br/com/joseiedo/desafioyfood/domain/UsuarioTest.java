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
    void persist_formasPagamentoEmpty_shouldThrowIllegalArgumentException() {
        Set<FormaPagamento> formasVazias = new HashSet<>();
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Usuario("usuario@test.com", formasVazias);
        });
    }

    @Test
    void persist_validUsuario_shouldPersistSuccessfully() {
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
    void persist_invalidEmail_shouldFailValidation() {
        FormaPagamento visa = FormaPagamento.VISA;
        
        assertThrows(Exception.class, () -> {
            Usuario usuario = new Usuario("email-inválido", Set.of(visa));
            usuarioRepository.saveAndFlush(usuario);
        });
    }

    @Test
    void persist_blankEmail_shouldFailValidation() {
        FormaPagamento visa = FormaPagamento.VISA;
        
        assertThrows(Exception.class, () -> {
            Usuario usuario = new Usuario("", Set.of(visa));
            usuarioRepository.saveAndFlush(usuario);
        });
    }
}