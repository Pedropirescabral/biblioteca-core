package br.com.biblioteca.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LivroTest {

    private Livro livro;

    @BeforeEach
    void setUp() {
        livro = new Livro(1L, "1984", "George Orwell", "978-0451524935", 1);
    }

    @Test
    @DisplayName("Livro recém-cadastrado com quantidade > 0 deve estar disponível")
    void livroNovoDeveEstarDisponivel() {
        assertTrue(livro.isDisponivel());
    }

    @Test
    @DisplayName("Livro sem exemplares não deve estar disponível")
    void livroSemExemplaresNaoDeveEstarDisponivel() {
        livro.reservarExemplar();
        assertFalse(livro.isDisponivel());
    }

    @Test
    @DisplayName("Não deve permitir reservar exemplar quando não há disponibilidade")
    void naoDevePermitirReservarSemDisponibilidade() {
        livro.reservarExemplar();
        assertThrows(IllegalStateException.class, () -> livro.reservarExemplar());
    }
}
