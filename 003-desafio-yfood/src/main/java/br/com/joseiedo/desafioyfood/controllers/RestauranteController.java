package br.com.joseiedo.desafioyfood.controllers;

import br.com.joseiedo.desafioyfood.domain.FormaPagamento;
import br.com.joseiedo.desafioyfood.domain.ProcessadorRegrasFraude;
import br.com.joseiedo.desafioyfood.domain.Restaurante;
import br.com.joseiedo.desafioyfood.domain.Usuario;
import br.com.joseiedo.desafioyfood.exceptions.NotFoundException;
import br.com.joseiedo.desafioyfood.repository.RestauranteRepository;
import br.com.joseiedo.desafioyfood.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProcessadorRegrasFraude processadorRegrasFraude;
    
    public RestauranteController(RestauranteRepository restauranteRepository, 
                               UsuarioRepository usuarioRepository,
                               ProcessadorRegrasFraude processadorRegrasFraude) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
        this.processadorRegrasFraude = processadorRegrasFraude;
    }
    
    @GetMapping("/{restauranteId}/formas-pagamento-compativeis/{usuarioId}")
    public ResponseEntity<List<FormaPagamentoResponseDto>> getFormasCompativeis(
            @PathVariable Long restauranteId, 
            @PathVariable Long usuarioId) {
        
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new NotFoundException(Restaurante.class, restauranteId));
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException(Usuario.class, usuarioId));
        
        Set<FormaPagamento> formasUsuarioFiltradas = processadorRegrasFraude.aplicarRegras(usuario);
        Set<FormaPagamento> formasCompativeis = restaurante.getFormasCompativeisCom(formasUsuarioFiltradas);
        
        List<FormaPagamentoResponseDto> response = formasCompativeis.stream()
                .map(FormaPagamentoResponseDto::new)
                .toList();
        
        return ResponseEntity.ok(response);
    }
}