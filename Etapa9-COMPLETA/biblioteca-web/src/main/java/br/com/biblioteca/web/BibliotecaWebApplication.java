package br.com.biblioteca.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação web (Spring Boot / Spring MVC).
 * Reaproveita integralmente as regras de negócio de biblioteca-core
 * (Etapa 6); aqui só existem controllers (MVC), repositórios JDBC e
 * configuração — nenhuma regra de negócio nova é criada nesta camada.
 */
@SpringBootApplication
public class BibliotecaWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(BibliotecaWebApplication.class, args);
    }
}
