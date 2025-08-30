package br.com.joseiedo.desafioyfood.domain;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FormaPagamentoTest {

    @Test
    void deveRetornarVazioQuandoUmConjuntoVazioEOutroComUmItem() {
        Set<FormaPagamento> empty = new HashSet<>();
        Set<FormaPagamento> oneItem = Set.of(FormaPagamento.VISA);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(empty, oneItem);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void deveRetornarVazioQuandoUmConjuntoComItemEOutroVazio() {
        Set<FormaPagamento> oneItem = Set.of(FormaPagamento.VISA);
        Set<FormaPagamento> empty = new HashSet<>();
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(oneItem, empty);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void deveRetornarVazioQuandoCadaConjuntoTemItensDistintos() {
        Set<FormaPagamento> set1 = Set.of(FormaPagamento.VISA);
        Set<FormaPagamento> set2 = Set.of(FormaPagamento.DINHEIRO);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(set1, set2);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void deveRetornarVazioQuandoAmbosConjuntosVazios() {
        Set<FormaPagamento> empty1 = new HashSet<>();
        Set<FormaPagamento> empty2 = new HashSet<>();
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(empty1, empty2);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void deveRetornarUmItemQuandoMesmoTipoEmCadaConjunto() {
        FormaPagamento forma1 = FormaPagamento.VISA;
        FormaPagamento forma2 = FormaPagamento.VISA;
        
        Set<FormaPagamento> set1 = Set.of(forma1);
        Set<FormaPagamento> set2 = Set.of(forma2);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(set1, set2);
        
        assertEquals(1, result.size());
        assertTrue(result.contains(forma1));
    }

    @Test
    void deveRetornarApenasItensCompativeisQuandoHaCorrespondenciaParcial() {
        FormaPagamento visa1 = FormaPagamento.VISA;
        FormaPagamento master1 = FormaPagamento.MASTER;
        FormaPagamento dinheiro1 = FormaPagamento.DINHEIRO;
        
        FormaPagamento visa2 = FormaPagamento.VISA;
        FormaPagamento elo2 = FormaPagamento.ELO;
        FormaPagamento dinheiro2 = FormaPagamento.DINHEIRO;
        
        Set<FormaPagamento> set1 = Set.of(visa1, master1, dinheiro1);
        Set<FormaPagamento> set2 = Set.of(visa2, elo2, dinheiro2);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(set1, set2);
        
        assertEquals(2, result.size()); // Only exact matches
        assertTrue(result.contains(visa1));
        assertTrue(result.contains(dinheiro1));
        assertFalse(result.contains(master1)); // MASTER doesn't match with VISA or ELO
    }

    @Test
    void deveRetornarVazioQuandoMultiplosItensSemCorrespondencia() {
        FormaPagamento visa = FormaPagamento.VISA;
        FormaPagamento master = FormaPagamento.MASTER;
        
        FormaPagamento dinheiro = FormaPagamento.DINHEIRO;
        FormaPagamento cheque = FormaPagamento.CHEQUE;
        
        Set<FormaPagamento> set1 = Set.of(visa, master);
        Set<FormaPagamento> set2 = Set.of(dinheiro, cheque);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(set1, set2);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void deveRetornarTodosDoprimeiroConjuntoQuandoTodosTiposCorrespondem() {
        FormaPagamento visa1 = FormaPagamento.VISA;
        FormaPagamento master1 = FormaPagamento.MASTER;
        FormaPagamento dinheiro1 = FormaPagamento.DINHEIRO;
        
        FormaPagamento visa2 = FormaPagamento.VISA;
        FormaPagamento master2 = FormaPagamento.MASTER;
        FormaPagamento dinheiro2 = FormaPagamento.DINHEIRO;
        
        Set<FormaPagamento> set1 = Set.of(visa1, master1, dinheiro1);
        Set<FormaPagamento> set2 = Set.of(visa2, master2, dinheiro2);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(set1, set2);
        
        assertEquals(3, result.size());
        assertTrue(result.contains(visa1));
        assertTrue(result.contains(master1));
        assertTrue(result.contains(dinheiro1));
    }

    @Test
    void deveCorresponderTodosCartoesQuandoConjuntoTemMultiplosTiposCartao() {
        FormaPagamento visa = FormaPagamento.VISA;
        FormaPagamento master = FormaPagamento.MASTER;
        FormaPagamento elo = FormaPagamento.ELO;
        
        FormaPagamento visaMatch = FormaPagamento.VISA;
        
        Set<FormaPagamento> set1 = Set.of(visa, master, elo);
        Set<FormaPagamento> set2 = Set.of(visaMatch);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(set1, set2);
        
        assertEquals(1, result.size()); // Only exact matches
        assertTrue(result.contains(visa));
        assertFalse(result.contains(master));
        assertFalse(result.contains(elo));
    }

    @Test
    void deveRetornarSeCorresponderQuandoUmItemContraMultiplos() {
        FormaPagamento visa = FormaPagamento.VISA;
        
        FormaPagamento visa1 = FormaPagamento.VISA;
        FormaPagamento master = FormaPagamento.MASTER;
        FormaPagamento elo = FormaPagamento.ELO;
        
        Set<FormaPagamento> singleSet = Set.of(visa);
        Set<FormaPagamento> multipleSet = Set.of(visa1, master, elo);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(singleSet, multipleSet);
        
        assertEquals(1, result.size());
        assertTrue(result.contains(visa));
    }

    @Test
    void deveLancarExcecaoQuandoParametroForNull() {
        FormaPagamento forma = FormaPagamento.VISA;
        Set<FormaPagamento> validSet = Set.of(forma);
        
        assertThrows(IllegalArgumentException.class, () -> {
            FormaPagamento.getFormasCompativeis(null, validSet);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            FormaPagamento.getFormasCompativeis(validSet, null);
        });
    }
}