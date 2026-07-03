package br.com.biblioteca.service;

import br.com.biblioteca.exception.LivroIndisponivelException;
import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.Membro;
import br.com.biblioteca.repository.impl.EmprestimoRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários de EmprestimoService, usando o repositório em memória
 * (dispensando banco de dados, conforme permitido no enunciado da Etapa 7).
 */
class EmprestimoServiceTest {

    private EmprestimoService emprestimoService;
    private Livro livro;
    private Membro membro;

    @BeforeEach
    void setUp() {
        emprestimoService = new EmprestimoService(new EmprestimoRepositoryMemoria(), new CalculadoraMulta());
        livro = new Livro(1L, "Effective Java", "Joshua Bloch", "978-0134685991", 1);
        membro = new Membro(1L, "Carlos Lima", "carlos@email.com", "M003");
    }

    @Test
    @DisplayName("Deve reservar um exemplar ao realizar um empréstimo")
    void deveReservarExemplarAoEmprestar() {
        emprestimoService.realizarEmprestimo(livro, membro, LocalDate.of(2026, 6, 1));

        assertFalse(livro.isDisponivel());
        assertEquals(0, livro.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar emprestar livro sem exemplares disponíveis")
    void deveLancarExcecaoQuandoLivroIndisponivel() {
        emprestimoService.realizarEmprestimo(livro, membro, LocalDate.of(2026, 6, 1));

        assertThrows(LivroIndisponivelException.class, () ->
                emprestimoService.realizarEmprestimo(livro, membro, LocalDate.of(2026, 6, 2)));
    }

    @Test
    @DisplayName("Deve devolver o exemplar ao registrar a devolução")
    void deveDevolverExemplarAoRegistrarDevolucao() {
        Emprestimo emprestimo = emprestimoService.realizarEmprestimo(livro, membro, LocalDate.of(2026, 6, 1));

        emprestimoService.registrarDevolucao(emprestimo.getId(), LocalDate.of(2026, 6, 5));

        assertTrue(livro.isDisponivel());
        assertEquals(1, livro.getQuantidadeDisponivel());
    }
}
