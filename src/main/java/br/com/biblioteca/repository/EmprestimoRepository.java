package br.com.biblioteca.repository;

import br.com.biblioteca.model.Emprestimo;
import java.util.List;
import java.util.Optional;

public interface EmprestimoRepository {
    Emprestimo salvar(Emprestimo emprestimo);
    Optional<Emprestimo> buscarPorId(Long id);
    List<Emprestimo> listarTodos();
    List<Emprestimo> listarAtivosPorMembro(Long membroId);
    void atualizar(Emprestimo emprestimo);
}
