package br.com.biblioteca.web.controller;

import br.com.biblioteca.service.LivroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping("/livros")
    public String listar(Model model) {
        model.addAttribute("livros", livroService.listarTodos());
        return "livros";
    }

    @PostMapping("/livros")
    public String cadastrar(@RequestParam String titulo,
                             @RequestParam String autor,
                             @RequestParam(required = false) String isbn,
                             @RequestParam int quantidade,
                             Model model) {
        try {
            livroService.cadastrar(titulo, autor, isbn, quantidade);
            model.addAttribute("mensagemSucesso", "Livro \"" + titulo + "\" cadastrado com sucesso.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensagemErro", e.getMessage());
        }
        model.addAttribute("livros", livroService.listarTodos());
        return "livros";
    }
}
