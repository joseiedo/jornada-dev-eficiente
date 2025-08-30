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
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        Set<FormaPagamento> formas = Set.of(visa);
        Usuario usuario = new Usuario("usuario@test.com", formas);
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        
        assertNotNull(usuarioSalvo);
        assertNotNull(usuarioSalvo.getId());
        assertEquals("usuario@test.com", usuarioSalvo.getEmail());
        assertEquals(1, usuarioSalvo.getFormasPagamento().size());
    }

    @Test
    void persist_usuarioWithMultipleFormasPagamento_shouldPersistSuccessfully() {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        FormaPagamento master = new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master Débito");
        FormaPagamento dinheiro = new FormaPagamento(TipoPagamento.DINHEIRO, "Dinheiro");
        
        Usuario usuario = new Usuario("usuario@test.com", Set.of(visa));
        usuario.adicionarFormaPagamento(master);
        usuario.adicionarFormaPagamento(dinheiro);
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        
        assertNotNull(usuarioSalvo);
        assertNotNull(usuarioSalvo.getId());
        assertEquals(3, usuarioSalvo.getFormasPagamento().size());
    }

    @Test
    void removerFormaPagamento_withMultipleForms_shouldRemoveSuccessfully() {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        FormaPagamento master = new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master Débito");
        
        Usuario usuario = new Usuario("usuario@test.com", Set.of(visa));
        usuario.adicionarFormaPagamento(master);
        
        usuario.removerFormaPagamento(master);
        
        assertEquals(1, usuario.getFormasPagamento().size());
        assertTrue(usuario.getFormasPagamento().contains(visa));
        assertFalse(usuario.getFormasPagamento().contains(master));
    }

    @Test
    void removerFormaPagamento_withOnlyOneForm_shouldThrowIllegalStateException() {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        Usuario usuario = new Usuario("usuario@test.com", Set.of(visa));
        
        assertThrows(IllegalStateException.class, () -> {
            usuario.removerFormaPagamento(visa);
        });
    }

    @Test
    void getFormasCompatíveisCom_withMatchingTypes_shouldReturnCompatibleForms() {
        FormaPagamento visaUsuario = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        FormaPagamento masterUsuario = new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master Débito");
        
        Usuario usuario = new Usuario("usuario@test.com", Set.of(visaUsuario));
        usuario.adicionarFormaPagamento(masterUsuario);
        
        FormaPagamento visaRestaurante = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Aceito");
        FormaPagamento eloRestaurante = new FormaPagamento(TipoPagamento.CARTAO_ELO, "Elo Aceito");
        Set<FormaPagamento> formasAceitas = Set.of(visaRestaurante, eloRestaurante);
        
        Set<FormaPagamento> formasCompativeis = usuario.getFormasCompatíveisCom(formasAceitas);
        
        assertEquals(1, formasCompativeis.size());
        assertTrue(formasCompativeis.contains(visaUsuario));
        assertFalse(formasCompativeis.contains(masterUsuario));
    }

    @Test
    void persist_invalidEmail_shouldFailValidation() {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        
        assertThrows(Exception.class, () -> {
            Usuario usuario = new Usuario("email-inválido", Set.of(visa));
            usuarioRepository.saveAndFlush(usuario);
        });
    }

    @Test
    void persist_blankEmail_shouldFailValidation() {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        
        assertThrows(Exception.class, () -> {
            Usuario usuario = new Usuario("", Set.of(visa));
            usuarioRepository.saveAndFlush(usuario);
        });
    }
}