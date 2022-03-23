package fun.spring.cc.util;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface PasswordEncryptor {

    // Helper interface, provides an easy way to generate encrypted passwords for the manual creation of users
    static void main(String args[]) {
        encryptPassword("123");
    }

    static void encryptPassword(String password) {
        PasswordEncoder encoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();

        System.out.println("Raw password:");
        System.out.println(password);
        System.out.println("---");
        System.out.println("Encrypted password:");
        System.out.println(encoder.encode(password));
    }

}
