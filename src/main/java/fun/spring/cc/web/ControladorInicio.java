package fun.spring.cc.web;

import fun.spring.cc.domain.Persona;
// import org.springframework.beans.factory.annotation.Value;
import fun.spring.cc.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

// import java.util.Arrays;
import javax.validation.Valid;
import java.util.List;

// @RestController sirve para controladores Rest
@Controller // @Controller sirve para controladores de tipo MVC
@RequestMapping
public class ControladorInicio {

    @Autowired
    private PersonaService personaService;

    @GetMapping("/")
    public String inicio(Model model,
        // Spring injecta el usuario logueado automáticamente:
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
        // El objeto de tipo persona indica a Spring que tiene que buscar uno,
        // si no lo encuentra crea un nuevo objeto vacío. En cualquier caso
        // Spring lo comparte automáticamente así que no es necesario usar model
        return "modificar";
    }

    @GetMapping("/editar/{id}")
    public String editar(Persona persona, Model model) {
        // Spring toma la {id} del path y lo utiliza para crear una Persona vacía
        // con esa id, siempre y cuando "id" sea el nombre de un atributo de Persona.
        // Actualizamos este objeto de tipo persona con la función que escribimos en
        // personaServicio, que toma la id de un objeto de tipo persona y lo busca en
        // la db. Finalmente lo agregamos a un atributo en nuestro modelo y vamos a la
        // página del formulario.
        persona = personaService.getPersona(persona);
        model.addAttribute("persona", persona);
        return "modificar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Persona persona, Errors errores) {
        // El atributo que queremos validar (con @Valid) y los errores deben estar
        // juntos, en ese orden. Comprobamos si hay errores con hasErrors(), y si los
        // hay volvemos a modificar. Si no, guardamos el objeto persona y redirect.
        if(errores.hasErrors()) {
            return "modificar";
        }
        personaService.guardar(persona);
        return "redirect:/";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(Persona persona) {
        // Aquí estamos usando path variable, como arriba. Si usamos query params,
        // tenemos que borrar el /{id} del path, y nada más. Spring es así de cape.
        personaService.eliminar(persona);
        return "redirect:/";
    }

}
