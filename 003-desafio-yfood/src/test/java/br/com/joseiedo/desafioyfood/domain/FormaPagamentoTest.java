package br.com.joseiedo.desafioyfood.domain;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FormaPagamentoTest {

    @Test
    void getFormasCompativeis_emptyAndOneItem_shouldReturnEmpty() {
        Set<FormaPagamento> empty = new HashSet<>();
        Set<FormaPagamento> oneItem = Set.of(
            new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito")
        );
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(empty, oneItem);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void getFormasCompativeis_oneItemAndEmpty_shouldReturnEmpty() {
        Set<FormaPagamento> oneItem = Set.of(
            new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito")
        );
        Set<FormaPagamento> empty = new HashSet<>();
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(oneItem, empty);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void getFormasCompativeis_oneDifferentItemEach_shouldReturnEmpty() {
        Set<FormaPagamento> set1 = Set.of(
            new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito")
        );
        Set<FormaPagamento> set2 = Set.of(
            new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master Crédito")
        );
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(set1, set2);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void getFormasCompativeis_bothEmpty_shouldReturnEmpty() {
        Set<FormaPagamento> empty1 = new HashSet<>();
        Set<FormaPagamento> empty2 = new HashSet<>();
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(empty1, empty2);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void getFormasCompativeis_sameTipoOneItemEach_shouldReturnOneItem() {
        FormaPagamento forma1 = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        FormaPagamento forma2 = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Débito");
        
        Set<FormaPagamento> set1 = Set.of(forma1);
        Set<FormaPagamento> set2 = Set.of(forma2);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(set1, set2);
        
        assertEquals(1, result.size());
        assertTrue(result.contains(forma1));
    }

    @Test
    void getFormasCompativeis_multipleItemsWithPartialMatch_shouldReturnOnlyMatching() {
        FormaPagamento visa1 = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        FormaPagamento master1 = new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master Crédito");
        FormaPagamento dinheiro1 = new FormaPagamento(TipoPagamento.DINHEIRO, "Dinheiro");
        
        FormaPagamento visa2 = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Débito");
        FormaPagamento elo2 = new FormaPagamento(TipoPagamento.CARTAO_ELO, "Elo Crédito");
        FormaPagamento dinheiro2 = new FormaPagamento(TipoPagamento.DINHEIRO, "Dinheiro");
        
        Set<FormaPagamento> set1 = Set.of(visa1, master1, dinheiro1);
        Set<FormaPagamento> set2 = Set.of(visa2, elo2, dinheiro2);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(set1, set2);
        
        assertEquals(2, result.size());
        assertTrue(result.contains(visa1));
        assertTrue(result.contains(dinheiro1));
        assertFalse(result.contains(master1));
    }

    @Test
    void getFormasCompativeis_multipleItemsNoMatch_shouldReturnEmpty() {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        FormaPagamento master = new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master Crédito");
        
        FormaPagamento elo = new FormaPagamento(TipoPagamento.CARTAO_ELO, "Elo Crédito");
        FormaPagamento hypercard = new FormaPagamento(TipoPagamento.CARTAO_HYPERCARD, "Hypercard");
        
        Set<FormaPagamento> set1 = Set.of(visa, master);
        Set<FormaPagamento> set2 = Set.of(elo, hypercard);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(set1, set2);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void getFormasCompativeis_allTypesMatch_shouldReturnAllFromFirstSet() {
        FormaPagamento visa1 = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        FormaPagamento master1 = new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master Crédito");
        FormaPagamento dinheiro1 = new FormaPagamento(TipoPagamento.DINHEIRO, "Dinheiro");
        
        FormaPagamento visa2 = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Débito");
        FormaPagamento master2 = new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master Débito");
        FormaPagamento dinheiro2 = new FormaPagamento(TipoPagamento.DINHEIRO, "Dinheiro");
        
        Set<FormaPagamento> set1 = Set.of(visa1, master1, dinheiro1);
        Set<FormaPagamento> set2 = Set.of(visa2, master2, dinheiro2);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(set1, set2);
        
        assertEquals(3, result.size());
        assertTrue(result.contains(visa1));
        assertTrue(result.contains(master1));
        assertTrue(result.contains(dinheiro1));
    }

    @Test
    void getFormasCompativeis_duplicateTypesInSameSet_shouldHandleCorrectly() {
        FormaPagamento visa1 = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        FormaPagamento visa2 = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Débito");
        FormaPagamento master = new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master");
        
        FormaPagamento visaMatch = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Premium");
        
        Set<FormaPagamento> set1 = Set.of(visa1, visa2, master);
        Set<FormaPagamento> set2 = Set.of(visaMatch);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(set1, set2);
        
        assertEquals(2, result.size()); // Both visa1 and visa2 should match
        assertTrue(result.contains(visa1));
        assertTrue(result.contains(visa2));
        assertFalse(result.contains(master));
    }

    @Test
    void getFormasCompativeis_singleItemAgainstMultiple_shouldReturnIfMatch() {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Único");
        
        FormaPagamento visa1 = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa 1");
        FormaPagamento master = new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master");
        FormaPagamento elo = new FormaPagamento(TipoPagamento.CARTAO_ELO, "Elo");
        
        Set<FormaPagamento> singleSet = Set.of(visa);
        Set<FormaPagamento> multipleSet = Set.of(visa1, master, elo);
        
        Set<FormaPagamento> result = FormaPagamento.getFormasCompativeis(singleSet, multipleSet);
        
        assertEquals(1, result.size());
        assertTrue(result.contains(visa));
    }

    @Test
    void getFormasCompativeis_nullHandling_shouldThrowIllegalArgumentException() {
        FormaPagamento forma = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa");
        Set<FormaPagamento> validSet = Set.of(forma);
        
        assertThrows(IllegalArgumentException.class, () -> {
            FormaPagamento.getFormasCompativeis(null, validSet);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            FormaPagamento.getFormasCompativeis(validSet, null);
        });
    }
}