package br.com.biblioteca.service;

import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.Membro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes unitários da regra de cálculo de multa (funcionalidade de cálculo
 * simples, sem dependência de banco de dados, conforme recomendado no
 * enunciado da Etapa 7).
 */
class CalculadoraMultaTest {

    private CalculadoraMulta calculadoraMulta;
    private Livro livro;
    private Membro membro;

    @BeforeEach
    void setUp() {
        calculadoraMulta = new CalculadoraMulta();
        livro = new Livro(1L, "Refactoring", "Martin Fowler", "978-0134757599", 1);
        membro = new Membro(1L, "Maria Souza", "maria@email.com", "M002");
    }

    @Test
    @DisplayName("Não deve gerar multa quando a devolução ocorre dentro do prazo")
    void naoDeveGerarMultaQuandoDevolucaoNoPrazo() {
        Emprestimo emprestimo = new Emprestimo(1L, livro, membro, LocalDate.of(2026, 6, 1), 7);

        double multa = calculadoraMulta.calcular(emprestimo, LocalDate.of(2026, 6, 8));

        assertEquals(0.0, multa, 0.001);
    }

    @Test
    @DisplayName("Deve cobrar R$ 0,50 por dia de atraso na devolução")
    void deveCobrarMultaProporcionalAosDiasDeAtraso() {
        Emprestimo emprestimo = new Emprestimo(1L, livro, membro, LocalDate.of(2026, 6, 1), 7);
        // previsão: 2026-06-08 ; devolvido em 2026-06-12 -> 4 dias de atraso

        double multa = calculadoraMulta.calcular(emprestimo, LocalDate.of(2026, 6, 12));

        assertEquals(2.00, multa, 0.001);
    }

    @Test
    @DisplayName("Deve considerar exatamente a data prevista como não atrasada")
    void dataPrevistaNaoDeveGerarMulta() {
        Emprestimo emprestimo = new Emprestimo(1L, livro, membro, LocalDate.of(2026, 6, 1), 7);

        double multa = calculadoraMulta.calcular(emprestimo, LocalDate.of(2026, 6, 8));

        assertEquals(0.0, multa, 0.001);
    }
}
