package az.tourmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TourmateBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TourmateBackendApplication.class, args);
    }

}
