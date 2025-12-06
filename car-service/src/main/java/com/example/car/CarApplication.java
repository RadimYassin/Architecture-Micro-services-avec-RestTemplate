package com.example.car;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // Active l'enregistrement auprès d'Eureka
public class CarApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarApplication.class, args);
    }

    @org.springframework.context.annotation.Bean
    org.springframework.boot.CommandLineRunner commandLineRunner(com.example.car.repositories.CarRepository carRepository) {
        return args -> {
            carRepository.save(new com.example.car.entities.Car(null, "Toyota", "Corolla", "A123", 1L));
            carRepository.save(new com.example.car.entities.Car(null, "Renault", "Clio", "B456", 2L));
            carRepository.save(new com.example.car.entities.Car(null, "Peugeot", "308", "C789", 1L));
            
            carRepository.findAll().forEach(c -> {
                System.out.println(c.toString());
            });
        };
    }
    
    /**
     * Configure RestTemplate pour la communication inter-services
     * @return Instance configurée de RestTemplate
     */
    @org.springframework.context.annotation.Bean
    public org.springframework.web.client.RestTemplate restTemplate() {
        org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
        
        // Configuration des timeouts
        org.springframework.http.client.SimpleClientHttpRequestFactory requestFactory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);  // 5 secondes pour la connexion
        requestFactory.setReadTimeout(5000);     // 5 secondes pour la lecture
        
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}
