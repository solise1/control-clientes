package fun.spring.cc.web;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //  Spring va a inyectar de manera automática nuestro UsuarioService porque lo
    //  llamamos "userDetailsService" en la anotación.
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // void configureGlobal() recibe un AuthenticationManagerBuilder, y por nuestra
    // anotación Spring lo inyecta automáticamente. nuestro builder utiliza dos métodos:
    // 1) userDetailsService(), que recibe nuestro objeto de servicio, y
    // 2) passwordEncoder(), que recibe un nuevo BCryptPasswordEncoder.
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


/*
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
*/
    // == Autenticación (login) ==
    // La Autenticación nos permite controlar las credenciales de los usuarios, como
    // su nombre de usuario, su password y los roles que poseen (como ADMIN o USER).
    // Nuestra función void configure(AuthenticationManagerBuilder auth) nos permite
    // quitar el usuario que viene por defecto y nos permite agregar más usuarios y
    // personalizarlos para hacer login.
/*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Usuarios desde memoria (nada de db)
        auth.inMemoryAuthentication()
                .withUser("admin")
                    .password("{noop}123")
                    .roles("ADMIN", "USER")
                .and()
                .withUser("user")
                    .password("{noop}123")
                    .roles("USER");
    }
*/

    // == Autorización ==
    // Restringe las URLs de nuestra aplicación web de acuerdo a sus perms.
    // Reemplazamos el método void configure(HttpSecurity http) e invocamos el
    // método authorizeRequests() para restringir el acceso a cada path.
    // antMatchers() recibe los paths, y luego lo encadenamos con hasRole() o con
    // hasAnyRole() (si queremos declarar más de uno). En nuestro código restringimos
    // los paths de create-update-delete a admin, y le otorgamos read tanto a admin
    // como a user.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .authorizeRequests()
                    .antMatchers("/")
                        .authenticated()
//                        .hasAnyRole("USER", "ADMIN")
                    .antMatchers("/editar/**", "/agregar", "/eliminar/**")
                        .hasRole("ADMIN")
            // Aquí indicamos que vamos a utilizar un formulario de ingreso personalizado
                .and()
                .formLogin()
                    .loginPage("/login") // plantilla en templates/login
                    .permitAll()
                .defaultSuccessUrl("/", true)
            // Aquí indicamos la ruta de la vista para la excepción 403 (forbidden)
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login")
                .and()
                .exceptionHandling().accessDeniedPage("/errores/403");
    }
}
