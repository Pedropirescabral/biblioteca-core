package br.com.biblioteca.web.config;

import br.com.biblioteca.repository.EmprestimoRepository;
import br.com.biblioteca.repository.LivroRepository;
import br.com.biblioteca.repository.MembroRepository;
import br.com.biblioteca.service.CalculadoraMulta;
import br.com.biblioteca.service.EmprestimoService;
import br.com.biblioteca.service.LivroService;
import br.com.biblioteca.service.MembroService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Composition root da aplicação web: é aqui, e somente aqui, que as classes
 * de serviço de biblioteca-core são conectadas às implementações JDBC dos
 * repositórios (@Repository, injetadas automaticamente pelo Spring). Os
 * Services em si (LivroService, MembroService, EmprestimoService) são os
 * MESMOS objetos criados manualmente em Main.java na Etapa 6 — nenhuma
 * regra de negócio foi reescrita para a Web.
 */
@Configuration
public class AppConfig {

    @Bean
    public LivroService livroService(LivroRepository livroRepository) {
        return new LivroService(livroRepository);
    }

    @Bean
    public MembroService membroService(MembroRepository membroRepository) {
        return new MembroService(membroRepository);
    }

    @Bean
    public CalculadoraMulta calculadoraMulta() {
        return new CalculadoraMulta();
    }

    @Bean
    public EmprestimoService emprestimoService(EmprestimoRepository emprestimoRepository, CalculadoraMulta calculadoraMulta) {
        return new EmprestimoService(emprestimoRepository, calculadoraMulta);
    }
}
