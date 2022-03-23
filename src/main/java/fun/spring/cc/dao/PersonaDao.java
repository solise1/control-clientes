package fun.spring.cc.dao;

import fun.spring.cc.domain.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

// alternativa: anotar @Repository y extender de JpaRepository en vez de CrudRepository
public interface PersonaDao extends JpaRepository<Persona, Long> {
}
