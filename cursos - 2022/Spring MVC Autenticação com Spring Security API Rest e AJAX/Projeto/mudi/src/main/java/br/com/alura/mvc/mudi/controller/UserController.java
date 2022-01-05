package br.com.alura.mvc.mudi.controller;

import br.com.alura.mvc.mudi.entities.Pedido;
import br.com.alura.mvc.mudi.entities.enums.StatusPedido;
import br.com.alura.mvc.mudi.repository.PedidoRepository;
import br.com.alura.mvc.mudi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping()
    public String home(Model model, Principal principal) {
        List<Pedido> pedidos = userRepository.findByStatusAndUser(StatusPedido.ENTREGUE, principal.getName());

        model.addAttribute("pedidos", pedidos);
        return "pedido";
    }

    @GetMapping("/{status}")
    public String porStatus(@PathVariable("status") String status, Model model) {
        try{
            List<Pedido> pedidos = pedidoRepository.findByStatus(StatusPedido.valueOf(status.toUpperCase()), PageRequest.of(0,5, Sort.by("dataDaEntrega").descending()));
            model.addAttribute("pedidos", pedidos);
            model.addAttribute("status", status);

            return "pedido";
        }catch(IllegalArgumentException e){
            return "redirect:pedido";
        }
    }

}
