package br.com.alura.mvc.mudi.api;


import br.com.alura.mvc.mudi.dto.PedidoDTO;
import br.com.alura.mvc.mudi.entities.Pedido;
import br.com.alura.mvc.mudi.entities.enums.StatusPedido;
import br.com.alura.mvc.mudi.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoRestController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/aguardando")
    public List<Pedido> getPedidosAguardandoOferta(){
        Sort sort = Sort.by("id").descending();
        PageRequest paginacao = PageRequest.of(0, 10 , sort);


        //Caso for DTO
//        return pedidoRepository
//                .findByStatus(StatusPedido.AGUARDANDO, paginacao)
//                .stream()
//                .map(x-> new PedidoDTO().toDto(x)).collect(Collectors.toList());

        return pedidoRepository.findByStatus(StatusPedido.AGUARDANDO, paginacao);
    }
}
