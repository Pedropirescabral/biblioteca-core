package br.com.biblioteca.repository;

import br.com.biblioteca.model.Livro;
import java.util.List;
import java.util.Optional;

/**
 * Abstração de acesso a dados de Livro (DIP: os Services dependem desta
 * interface, nunca de uma implementação concreta). Permite trocar a forma de
 * persistência (memória, JDBC, JPA...) sem alterar as regras de negócio.
 */
public interface LivroRepository {
    Livro salvar(Livro livro);
    Optional<Livro> buscarPorId(Long id);
    List<Livro> listarTodos();
    List<Livro> listarDisponiveis();
    void atualizar(Livro livro);
}
