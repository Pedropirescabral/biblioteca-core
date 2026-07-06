package br.com.biblioteca.web.controller;

import br.com.biblioteca.service.EmprestimoService;
import br.com.biblioteca.service.MembroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MembroController {

    private final MembroService membroService;
    private final EmprestimoService emprestimoService;

    public MembroController(MembroService membroService, EmprestimoService emprestimoService) {
        this.membroService = membroService;
        this.emprestimoService = emprestimoService;
    }

    @GetMapping("/membros")
    public String listar(Model model) {
        var membros = membroService.listarTodos();
        Map<Long, Integer> ativosPorMembro = new HashMap<>();
        membros.forEach(m -> ativosPorMembro.put(m.getId(), emprestimoService.listarAtivosPorMembro(m.getId()).size()));

        model.addAttribute("membros", membros);
        model.addAttribute("ativosPorMembro", ativosPorMembro);
        return "membros";
    }

    @PostMapping("/membros")
    public String cadastrar(@RequestParam String nome,
                             @RequestParam String email,
                             @RequestParam String matricula,
                             Model model) {
        try {
            membroService.cadastrar(nome, email, matricula);
            model.addAttribute("mensagemSucesso", "Membro \"" + nome + "\" cadastrado com sucesso.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensagemErro", e.getMessage());
        }
        return listar(model);
    }
}
