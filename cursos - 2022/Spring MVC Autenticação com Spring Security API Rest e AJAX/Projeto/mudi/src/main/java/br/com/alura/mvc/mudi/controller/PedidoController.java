package br.com.alura.mvc.mudi.controller;

import br.com.alura.mvc.mudi.dto.PedidoDTO;
import br.com.alura.mvc.mudi.entities.Pedido;
import br.com.alura.mvc.mudi.entities.User;
import br.com.alura.mvc.mudi.entities.enums.StatusPedido;
import br.com.alura.mvc.mudi.repository.PedidoRepository;
import br.com.alura.mvc.mudi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/formulario")
    public String formulario(PedidoDTO pedidoDTO) {
        return "pedido/formulario";
    }

    @PostMapping("/novo")
    public String novo(@Valid PedidoDTO pedidoDTO, BindingResult result, Principal principal) {
        if (result.hasErrors())
            return "pedido/formulario";

        //String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(principal.getName());

        Pedido pedido = new Pedido();
        pedido.setUser(user);
        pedido.toPedido(pedidoDTO);
        pedido.setStatus(StatusPedido.AGUARDANDO);

        pedidoRepository.save(pedido);
        return "redirect:/usuario";
    }
}
