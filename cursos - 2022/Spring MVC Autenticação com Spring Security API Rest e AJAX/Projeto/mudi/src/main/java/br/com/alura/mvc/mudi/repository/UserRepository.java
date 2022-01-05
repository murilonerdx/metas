package br.com.alura.mvc.mudi.repository;

import br.com.alura.mvc.mudi.entities.Pedido;
import br.com.alura.mvc.mudi.entities.User;
import br.com.alura.mvc.mudi.entities.enums.StatusPedido;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Cacheable("user")
    @Query("select p from Pedido p join p.user u where u.username = :username and p.status = :status")
    List<Pedido> findByStatusAndUser(@Param("status") StatusPedido status, @Param("username") String username);
}
