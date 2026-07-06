package br.com.biblioteca.service;

import br.com.biblioteca.exception.MembroNaoEncontradoException;
import br.com.biblioteca.model.Membro;
import br.com.biblioteca.repository.MembroRepository;

import java.util.List;

public class MembroService {

    private final MembroRepository membroRepository;

    public MembroService(MembroRepository membroRepository) {
        this.membroRepository = membroRepository;
    }

    public Membro cadastrar(String nome, String email, String matricula) {
        Membro membro = new Membro(null, nome, email, matricula);
        return membroRepository.salvar(membro);
    }

    public Membro buscarPorId(Long id) {
        return membroRepository.buscarPorId(id)
                .orElseThrow(() -> new MembroNaoEncontradoException("Membro não encontrado: id=" + id));
    }

    public List<Membro> listarTodos() {
        return membroRepository.listarTodos();
    }
}
