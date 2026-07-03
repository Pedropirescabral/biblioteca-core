package br.com.biblioteca.service;

import br.com.biblioteca.exception.EmprestimoNaoEncontradoException;
import br.com.biblioteca.exception.LivroIndisponivelException;
import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.Membro;
import br.com.biblioteca.repository.EmprestimoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Regras de negócio de Empréstimo.
 *
 * No projeto desktop original (hipotético), o método salvarEmprestimo() do
 * JFrame fazia tudo junto: validava disponibilidade, calculava data de
 * devolução, gravava no banco e ainda atualizava componentes da tela
 * (code smell "Long Method" / "God Class"). Aqui essa responsabilidade foi
 * quebrada: EmprestimoService cuida só da regra de empréstimo/devolução,
 * CalculadoraMulta cuida só do cálculo de multa (Strategy), e a persistência
 * fica isolada em EmprestimoRepository (Repository pattern). Isso segue o
 * SRP e o DIP, e facilita reuso tanto no desktop quanto na versão web.
 */
public class EmprestimoService {

    private static final int DIAS_PADRAO_EMPRESTIMO = 7;

    private final EmprestimoRepository emprestimoRepository;
    private final CalculadoraMulta calculadoraMulta;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, CalculadoraMulta calculadoraMulta) {
        this.emprestimoRepository = emprestimoRepository;
        this.calculadoraMulta = calculadoraMulta;
    }

    public Emprestimo realizarEmprestimo(Livro livro, Membro membro, LocalDate dataEmprestimo) {
        if (!livro.isDisponivel()) {
            throw new LivroIndisponivelException(
                    "Não há exemplares disponíveis para o livro: " + livro.getTitulo());
        }
        livro.reservarExemplar();
        Emprestimo emprestimo = new Emprestimo(null, livro, membro, dataEmprestimo, DIAS_PADRAO_EMPRESTIMO);
        return emprestimoRepository.salvar(emprestimo);
    }

    public double registrarDevolucao(Long emprestimoId, LocalDate dataDevolucao) {
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(emprestimoId)
                .orElseThrow(() -> new EmprestimoNaoEncontradoException("Empréstimo não encontrado: id=" + emprestimoId));

        double multa = calculadoraMulta.calcular(emprestimo, dataDevolucao);
        emprestimo.registrarDevolucao(dataDevolucao);
        emprestimo.getLivro().devolverExemplar();
        emprestimoRepository.atualizar(emprestimo);
        return multa;
    }

    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.listarTodos();
    }

    public List<Emprestimo> listarAtivosPorMembro(Long membroId) {
        return emprestimoRepository.listarAtivosPorMembro(membroId);
    }
}
