package fun.spring.cc.dao;

import fun.spring.cc.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioDao extends JpaRepository<Usuario, Integer> {

    Usuario findByUsername(String username);

}
