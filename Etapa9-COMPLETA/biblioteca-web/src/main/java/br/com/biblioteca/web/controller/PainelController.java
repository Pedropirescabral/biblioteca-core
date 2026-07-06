package br.com.biblioteca.web.controller;

import br.com.biblioteca.model.StatusEmprestimo;
import br.com.biblioteca.service.EmprestimoService;
import br.com.biblioteca.service.LivroService;
import br.com.biblioteca.service.MembroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PainelController {

    private final LivroService livroService;
    private final MembroService membroService;
    private final EmprestimoService emprestimoService;

    public PainelController(LivroService livroService, MembroService membroService, EmprestimoService emprestimoService) {
        this.livroService = livroService;
        this.membroService = membroService;
        this.emprestimoService = emprestimoService;
    }

    @GetMapping("/")
    public String painel(Model model) {
        var livros = livroService.listarTodos();
        var emprestimos = emprestimoService.listarTodos();

        model.addAttribute("totalLivros", livros.size());
        model.addAttribute("totalDisponiveis", livros.stream().mapToInt(l -> l.getQuantidadeDisponivel()).sum());
        model.addAttribute("totalMembros", membroService.listarTodos().size());
        model.addAttribute("totalAtivos", emprestimos.stream().filter(e -> e.getStatus() == StatusEmprestimo.ATIVO).count());
        model.addAttribute("recentes", emprestimos.stream().limit(8).toList());
        return "painel";
    }
}
