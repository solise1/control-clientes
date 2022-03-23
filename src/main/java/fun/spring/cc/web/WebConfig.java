package fun.spring.cc.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver; // i18n = internationalization, i (18 letters) n

import java.util.Locale;

// Debemos anotar con @Configuration o nuestras configuraciones van a ser ignoradas
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Esta clase es parte del patrón MVC de Spring.

    // LocaleResolver es una interfaz de Spring que sirve para determinar el Locale.
    // Lo anotamos como @Bean para que Spring lo encuentre automáticamente.
    // Dentro instanciamos un SessionLocaleResolver (que es una implementación de la
    // interfaz), le indicamos el Locale que queremos por defecto y lo retornamos.
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
//        return new CookieLocaleResolver();
    }

    // Nuestro LocaleChangeInterceptor nos permite obtener un Locale, en este caso
    // desde query params (ex: ?lang=es). Esto nos permite crear y utilizar distintos
    // archivos de propiedades para la internacionalización.
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    // Finalmente sobreescribimos un método llamado addInterceptors que recibe un
    // objeto de tipo InterceptorRegistry, y luego llamamos addInterceptor() desde
    // el objeto pasándole localChangeInterceptor() como argumento. De este modo le
    // decimos a Spring que queremos que utilice nuestro interceptor.
    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(localeChangeInterceptor());
    }

    // Este método nos permite hacer el mapping de la url por default. Si el usuario
    // tiene permiso para ver "/", Spring redirecciona a index. Si no, a "/login".
    @Override
    public void addViewControllers(ViewControllerRegistry registro) {
        registro.addViewController("/").setViewName("index");
        registro.addViewController("/login");
        registro.addViewController("/errores/403").setViewName("/errores/403");
    }
}
