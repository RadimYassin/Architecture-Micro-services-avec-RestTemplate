package com.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // Active l'enregistrement auprès d'Eureka
public class ClientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientServiceApplication.class, args);
    }

    @org.springframework.context.annotation.Bean
    org.springframework.boot.CommandLineRunner commandLineRunner(com.example.client.repositories.ClientRepository clientRepository) {
        return args -> {
            clientRepository.save(new com.example.client.entities.Client(null, "Rania KABBAGE", 25f));
            clientRepository.save(new com.example.client.entities.Client(null, "Ahmed BENANI", 30f));
            clientRepository.save(new com.example.client.entities.Client(null, "Sarah IDRISSI", 28f));
            
            clientRepository.findAll().forEach(c -> {
                System.out.println(c.toString());
            });
        };
    }
}
