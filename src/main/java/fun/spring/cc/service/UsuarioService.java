package fun.spring.cc.service;

import fun.spring.cc.dao.UsuarioDao;
import fun.spring.cc.domain.Rol;
import fun.spring.cc.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

// Esta clase implementa la interfaz UserDetailsService, que nos permite mapear
// nuestras clases (en este caso, Usuario y Rol) en objetos que pueden ser comprendidos
// por Spring Security.
@Service("userDetailsService")
public class UsuarioService implements UserDetailsService {

    // Necesitamos un repo como dependencia, que Spring va a inyectar automáticamente.
    @Autowired
    private UsuarioDao usuarioDao;

    // UserDetails loadUserByUsername(String s) busca un usuario a través de una
    // string (el nombre de usuario). Si el usuario no existe arroja una excepción.
    // De otra manera, crea un arraylist de tipo GrantedAuthority, que es un objeto
    // que Spring Security utiliza como rol. Recorremos los Roles del usuario que
    // nosotres definimos, y finalmente creamos un nuevo SimpleGrantedAuthority a
    // partir del nombre de cada rol.
    // Finalmente retornamos un nuevo objeto User y le pasamos el nombre de usuario,
    // la contraseña y la lista de GrantedAuthority.
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Usuario usuario = usuarioDao.findByUsername(username);

        if (usuario == null) {
            System.out.println("Username not found");
            throw new UsernameNotFoundException(username);
        }

        ArrayList<GrantedAuthority> roles = new ArrayList<>();

        for(Rol rol : usuario.getRoles()) {
            roles.add(new SimpleGrantedAuthority(rol.getNombre().trim()));
        }

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(roles)
                .build();
    }

}
