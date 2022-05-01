package fun.spring.cc.web;

import fun.spring.cc.domain.Persona;
import fun.spring.cc.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping
public class ControladorInicio {
    @Autowired
    private PersonaService personaService;

    @GetMapping("/")
    public String inicio(Model model,
                         @AuthenticationPrincipal User user) {

        List<Persona> personas = personaService.getListaPersonas();

        double saldoTotal = personas.stream()
                                    .mapToDouble(p -> p.getSaldo())
                                    .sum();

        model.addAttribute("personas", personas);
        model.addAttribute("saldoTotal", saldoTotal);
        model.addAttribute("totalClientes", personas.size());

        return "index";
    }

    @GetMapping("/agregar")
    public String agregar(Persona persona) {
        return "modificar";
    }

    @GetMapping("/editar/{id}")
    public String editar(Persona persona, Model model) {
        persona = personaService.getPersona(persona);
        model.addAttribute("persona", persona);
        return "modificar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Persona persona, Errors errores) {
        if(errores.hasErrors()) {
            return "modificar";
        }
        personaService.guardar(persona);
        return "redirect:/";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(Persona persona) {
        personaService.eliminar(persona);
        return "redirect:/";
    }

}
