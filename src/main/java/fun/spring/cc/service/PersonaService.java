package fun.spring.cc.service;

import fun.spring.cc.dao.PersonaDao;
import fun.spring.cc.domain.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonaService {

    @Autowired
    private PersonaDao personaDao;

    @Transactional(readOnly = true)
    public List<Persona> getListaPersonas(){
        return (List<Persona>) personaDao.findAll();
    }

    @Transactional
    public void guardar(Persona persona){
        personaDao.save(persona);
    }

    @Transactional
    public void eliminar(Persona persona){
        personaDao.delete(persona);
    }

    @Transactional(readOnly = true)
    public Persona getPersona(Persona persona){
        return personaDao.findById(persona.getId()).orElse(null);
    }
}
