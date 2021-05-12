package kim.sihwan.daangn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DaangnApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaangnApplication.class, args);
    }

}
