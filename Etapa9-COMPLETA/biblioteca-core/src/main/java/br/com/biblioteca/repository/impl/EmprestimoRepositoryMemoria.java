package br.com.biblioteca.repository.impl;

import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.StatusEmprestimo;
import br.com.biblioteca.repository.EmprestimoRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class EmprestimoRepositoryMemoria implements EmprestimoRepository {

    private final Map<Long, Emprestimo> dados = new LinkedHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(1);

    @Override
    public Emprestimo salvar(Emprestimo emprestimo) {
        if (emprestimo.getId() == null) {
            emprestimo.setId(sequencia.getAndIncrement());
        }
        dados.put(emprestimo.getId(), emprestimo);
        return emprestimo;
    }

    @Override
    public Optional<Emprestimo> buscarPorId(Long id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<Emprestimo> listarTodos() {
        return new ArrayList<>(dados.values());
    }

    @Override
    public List<Emprestimo> listarAtivosPorMembro(Long membroId) {
        return dados.values().stream()
                .filter(e -> e.getMembro().getId().equals(membroId))
                .filter(e -> e.getStatus() == StatusEmprestimo.ATIVO)
                .collect(Collectors.toList());
    }

    @Override
    public void atualizar(Emprestimo emprestimo) {
        dados.put(emprestimo.getId(), emprestimo);
    }
}
