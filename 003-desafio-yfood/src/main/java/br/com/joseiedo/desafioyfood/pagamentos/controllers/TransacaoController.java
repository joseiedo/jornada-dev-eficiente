package br.com.joseiedo.desafioyfood.pagamentos.controllers;

import br.com.joseiedo.desafioyfood.pagamentos.domain.Transacao;
import br.com.joseiedo.desafioyfood.pagamentos.repository.TransacaoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
    
    private final TransacaoRepository transacaoRepository;
    
    public TransacaoController(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }
    
    @PostMapping
    public ResponseEntity<TransacaoResponseDto> criarTransacao(@Valid @RequestBody TransacaoRequestDto request) {
        Transacao transacao = request.toEntity();
        transacao = transacaoRepository.save(transacao);
        
        TransacaoResponseDto response = new TransacaoResponseDto(transacao);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<TransacaoResponseDto>> listarTransacoes() {
        List<TransacaoResponseDto> transacoes = transacaoRepository.findAll()
                .stream()
                .map(TransacaoResponseDto::new)
                .toList();
        
        return ResponseEntity.ok(transacoes);
    }
}