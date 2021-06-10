package demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Alex
 */
@SpringBootApplication
@MapperScan("com.app.saas.mapper")
public class SaasDemoApp {

    public static void main(String[] args) {
        SpringApplication.run(SaasDemoApp.class, args);
    }

}
