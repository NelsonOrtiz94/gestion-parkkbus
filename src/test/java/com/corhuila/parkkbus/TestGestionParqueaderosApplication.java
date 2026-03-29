package com.corhuila.parkkbus;

import org.springframework.boot.SpringApplication;

public class TestGestionParqueaderosApplication {

    public static void main(String[] args) {
        SpringApplication.from(GestionParqueaderosApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
