package br.com.joseiedo.desafioyfood.pagamentos.validators;

import br.com.joseiedo.desafioyfood.pagamentos.controllers.TransacaoRequestDto;
import br.com.joseiedo.desafioyfood.pagamentos.domain.FormaPagamento;
import br.com.joseiedo.desafioyfood.pagamentos.domain.RegraFraude;
import br.com.joseiedo.desafioyfood.pagamentos.domain.Restaurante;
import br.com.joseiedo.desafioyfood.pagamentos.domain.Usuario;
import br.com.joseiedo.desafioyfood.pagamentos.repository.RestauranteRepository;
import br.com.joseiedo.desafioyfood.pagamentos.repository.UsuarioRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CompatiblePaymentValidator implements ConstraintValidator<ValidCompatiblePayment, TransacaoRequestDto> {
    
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final Set<RegraFraude> regras;
    
    public CompatiblePaymentValidator(RestauranteRepository restauranteRepository, 
                                    UsuarioRepository usuarioRepository,
                                    Set<RegraFraude> regras) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
        this.regras = regras;
    }
    
    @Override
    public boolean isValid(TransacaoRequestDto request, ConstraintValidatorContext context) {
        if (request.restauranteId() == null || request.usuarioId() == null || request.formaPagamento() == null) {
            return true;
        }
        
        Restaurante restaurante = restauranteRepository.findById(request.restauranteId()).orElse(null);
        Usuario usuario = usuarioRepository.findById(request.usuarioId()).orElse(null);
        
        if (restaurante == null || usuario == null) {
            return true;
        }
        
        Set<FormaPagamento> formasCompativeis = restaurante.getFormasCompativeisComUsuario(usuario, regras);
        return formasCompativeis.contains(request.formaPagamento());
    }
}