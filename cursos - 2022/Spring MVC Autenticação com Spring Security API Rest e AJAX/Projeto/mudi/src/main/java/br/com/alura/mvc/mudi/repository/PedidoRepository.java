package br.com.alura.mvc.mudi.repository;

import br.com.alura.mvc.mudi.entities.Pedido;
import br.com.alura.mvc.mudi.entities.enums.StatusPedido;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Cacheable("pedido")
    List<Pedido> findByStatus(StatusPedido status, Pageable sort);

    @Cacheable("pedido")
    @Query("select p from Pedido p join p.user u where u.username = :username")
    List<Pedido> findAllByUsuario(@Param("username")String username);

//    @PersistenceContext
//    private EntityManager entityManager;

//    public List<Pedido> obterPedido(){
//        Query select_p_from_pedido_p = entityManager.createQuery("select p from Pedido p", Pedido.class);
//        return select_p_from_pedido_p.getResultList();
//    }

}
