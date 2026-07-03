package br.com.biblioteca.service;

import br.com.biblioteca.exception.LivroNaoEncontradoException;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.repository.LivroRepository;

import java.util.List;

/**
 * Regras de negócio relacionadas a Livro. Responsabilidade única (SRP):
 * cuida apenas de cadastro/consulta de livros, delegando a persistência
 * para LivroRepository (DIP - depende da abstração, injetada via construtor).
 */
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro cadastrar(String titulo, String autor, String isbn, int quantidade) {
        Livro livro = new Livro(null, titulo, autor, isbn, quantidade);
        return livroRepository.salvar(livro);
    }

    public Livro buscarPorId(Long id) {
        return livroRepository.buscarPorId(id)
                .orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado: id=" + id));
    }

    public List<Livro> listarTodos() {
        return livroRepository.listarTodos();
    }

    public List<Livro> listarDisponiveis() {
        return livroRepository.listarDisponiveis();
    }
}
