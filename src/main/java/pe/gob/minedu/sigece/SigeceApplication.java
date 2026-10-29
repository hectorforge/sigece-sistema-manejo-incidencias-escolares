package pe.gob.minedu.sigece;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SigeceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SigeceApplication.class, args);
    }

}
