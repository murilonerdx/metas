package br.com.alura.mvc.mudi.controller;

import br.com.alura.mvc.mudi.entities.Pedido;
import br.com.alura.mvc.mudi.entities.enums.StatusPedido;
import br.com.alura.mvc.mudi.repository.PedidoRepository;
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
@RequestMapping(value={"/home","/"})
public class HomeController {

    @Autowired
    private PedidoRepository repository;

    @GetMapping()
    public String home(Model model, Principal principal) {
        List<Pedido> pedidos = repository
                .findByStatus(StatusPedido.ENTREGUE, PageRequest.of(0,5, Sort.by("dataDaEntrega").descending()));
        model.addAttribute("pedidos", pedidos);
        return "home";
    }

}
