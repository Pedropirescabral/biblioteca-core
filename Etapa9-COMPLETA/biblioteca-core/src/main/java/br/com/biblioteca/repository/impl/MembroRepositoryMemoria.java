package br.com.biblioteca.repository.impl;

import br.com.biblioteca.model.Membro;
import br.com.biblioteca.repository.MembroRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class MembroRepositoryMemoria implements MembroRepository {

    private final Map<Long, Membro> dados = new LinkedHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(1);

    @Override
    public Membro salvar(Membro membro) {
        if (membro.getId() == null) {
            membro.setId(sequencia.getAndIncrement());
        }
        dados.put(membro.getId(), membro);
        return membro;
    }

    @Override
    public Optional<Membro> buscarPorId(Long id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<Membro> listarTodos() {
        return new ArrayList<>(dados.values());
    }
}
