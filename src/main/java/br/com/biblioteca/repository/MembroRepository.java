package br.com.biblioteca.repository;

import br.com.biblioteca.model.Membro;
import java.util.List;
import java.util.Optional;

public interface MembroRepository {
    Membro salvar(Membro membro);
    Optional<Membro> buscarPorId(Long id);
    List<Membro> listarTodos();
}
