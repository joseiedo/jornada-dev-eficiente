package br.com.joseiedo.desafioyfood.pagamentos.validators;

import br.com.joseiedo.desafioyfood.pagamentos.clients.PedidosClient;
import feign.FeignException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ExistingPedidoValidator implements ConstraintValidator<ValidExistingPedido, Long> {
    
    private final PedidosClient pedidosClient;
    
    public ExistingPedidoValidator(PedidosClient pedidosClient) {
        this.pedidosClient = pedidosClient;
    }
    
    @Override
    public boolean isValid(Long pedidoId, ConstraintValidatorContext context) {
        if (pedidoId == null) {
            return true;
        }
        
        try {
            pedidosClient.buscarPedido(pedidoId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        }
    }
}