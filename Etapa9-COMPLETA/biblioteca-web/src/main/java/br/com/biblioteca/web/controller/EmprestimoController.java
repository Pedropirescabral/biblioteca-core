package br.com.biblioteca.web.controller;

import br.com.biblioteca.exception.LivroIndisponivelException;
import br.com.biblioteca.exception.LivroNaoEncontradoException;
import br.com.biblioteca.exception.MembroNaoEncontradoException;
import br.com.biblioteca.service.EmprestimoService;
import br.com.biblioteca.service.LivroService;
import br.com.biblioteca.service.MembroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

/**
 * Orquestra o caso de uso "realizar empréstimo / registrar devolução" pela
 * web. As regras de negócio em si (verificar disponibilidade, calcular
 * multa) continuam inteiramente dentro de EmprestimoService (biblioteca-core).
 * O Controller só busca as entidades, chama o Service e garante que a
 * alteração de estado do Livro (reservarExemplar/devolverExemplar) seja
 * persistida, já que EmprestimoService não conhece LivroRepository (SRP).
 */
@Controller
public class EmprestimoController {

    private final EmprestimoService emprestimoService;
    private final LivroService livroService;
    private final MembroService membroService;

    public EmprestimoController(EmprestimoService emprestimoService, LivroService livroService, MembroService membroService) {
        this.emprestimoService = emprestimoService;
        this.livroService = livroService;
        this.membroService = membroService;
    }

    @GetMapping("/emprestimos")
    public String listar(Model model) {
        model.addAttribute("emprestimos", emprestimoService.listarTodos());
        model.addAttribute("livrosDisponiveis", livroService.listarDisponiveis());
        model.addAttribute("membros", membroService.listarTodos());
        model.addAttribute("hoje", LocalDate.now());
        return "emprestimos";
    }

    @PostMapping("/emprestimos")
    public String registrar(@RequestParam Long livroId,
                             @RequestParam Long membroId,
                             @RequestParam("dataEmprestimo") String dataEmprestimoTexto,
                             Model model) {
        try {
            var livro = livroService.buscarPorId(livroId);
            var membro = membroService.buscarPorId(membroId);
            LocalDate data = LocalDate.parse(dataEmprestimoTexto);

            emprestimoService.realizarEmprestimo(livro, membro, data);
            livroService.atualizar(livro); // persiste a baixa no estoque

            model.addAttribute("mensagemSucesso", "Empréstimo registrado com sucesso.");
        } catch (LivroIndisponivelException | LivroNaoEncontradoException | MembroNaoEncontradoException e) {
            model.addAttribute("mensagemErro", e.getMessage());
        }
        return listar(model);
    }

    @PostMapping("/emprestimos/{id}/devolucao")
    public String devolver(@org.springframework.web.bind.annotation.PathVariable Long id, Model model) {
        var emprestimo = emprestimoService.listarTodos().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow();

        double multa = emprestimoService.registrarDevolucao(id, LocalDate.now());
        livroService.atualizar(emprestimo.getLivro()); // persiste a volta ao estoque

        model.addAttribute("mensagemSucesso",
                multa > 0
                        ? String.format("Devolução registrada. Multa por atraso: R$ %.2f", multa)
                        : "Devolução registrada. Sem multa.");
        return listar(model);
    }
}
