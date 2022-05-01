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

@Service("userDetailsService")
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioDao usuarioDao;

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
