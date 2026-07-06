package br.com.biblioteca.repository.impl;

import br.com.biblioteca.model.Livro;
import br.com.biblioteca.repository.LivroRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Implementação em memória de LivroRepository. Útil para testes e para
 * demonstração via main(). Pode ser substituída por uma implementação JDBC
 * (ver LivroRepositoryJdbc no projeto web da Etapa 9) sem que LivroService
 * precise mudar uma única linha — princípio Open/Closed em ação.
 */
public class LivroRepositoryMemoria implements LivroRepository {

    private final Map<Long, Livro> dados = new LinkedHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(1);

    @Override
    public Livro salvar(Livro livro) {
        if (livro.getId() == null) {
            livro.setId(sequencia.getAndIncrement());
        }
        dados.put(livro.getId(), livro);
        return livro;
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<Livro> listarTodos() {
        return new ArrayList<>(dados.values());
    }

    @Override
    public List<Livro> listarDisponiveis() {
        return dados.values().stream()
                .filter(Livro::isDisponivel)
                .collect(Collectors.toList());
    }

    @Override
    public void atualizar(Livro livro) {
        dados.put(livro.getId(), livro);
    }
}
