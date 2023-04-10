package org.web3.indexer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "org.web3")
@EnableJpaRepositories("org.web3")
@EntityScan("org.web3")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}