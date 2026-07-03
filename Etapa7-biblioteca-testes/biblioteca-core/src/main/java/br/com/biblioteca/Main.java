package br.com.biblioteca;

import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.Membro;
import br.com.biblioteca.repository.impl.EmprestimoRepositoryMemoria;
import br.com.biblioteca.repository.impl.LivroRepositoryMemoria;
import br.com.biblioteca.repository.impl.MembroRepositoryMemoria;
import br.com.biblioteca.service.CalculadoraMulta;
import br.com.biblioteca.service.EmprestimoService;
import br.com.biblioteca.service.LivroService;
import br.com.biblioteca.service.MembroService;

import java.time.LocalDate;

/**
 * Classe de teste manual (smoke test) executada via main(), conforme pedido
 * no enunciado da Etapa 6, para demonstrar que as classes refatoradas
 * funcionam corretamente de ponta a ponta.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== TESTE MANUAL - SISTEMA DE BIBLIOTECA (Core refatorado) ===\n");

        // Composition root: aqui é o único lugar que conhece as implementações
        // concretas dos repositórios. Os Services só enxergam as interfaces.
        LivroService livroService = new LivroService(new LivroRepositoryMemoria());
        MembroService membroService = new MembroService(new MembroRepositoryMemoria());
        EmprestimoService emprestimoService = new EmprestimoService(
                new EmprestimoRepositoryMemoria(), new CalculadoraMulta());

        // 1) Cadastro de livros
        Livro livro1 = livroService.cadastrar("Clean Code", "Robert C. Martin", "978-0132350884", 2);
        Livro livro2 = livroService.cadastrar("Domain-Driven Design", "Eric Evans", "978-0321125217", 1);
        System.out.println("Livros cadastrados:");
        livroService.listarTodos().forEach(System.out::println);

        // 2) Cadastro de membro
        Membro joao = membroService.cadastrar("João Silva", "joao@email.com", "M001");
        System.out.println("\nMembro cadastrado: " + joao);

        // 3) Empréstimo dentro do prazo
        Emprestimo emp1 = emprestimoService.realizarEmprestimo(livro1, joao, LocalDate.of(2026, 6, 1));
        System.out.println("\nEmpréstimo realizado: " + emp1);
        System.out.println("Livro após empréstimo: " + livro1);

        // 4) Tentativa de emprestar livro sem estoque (DDD só tem 1 exemplar)
        emprestimoService.realizarEmprestimo(livro2, joao, LocalDate.of(2026, 6, 1));
        try {
            emprestimoService.realizarEmprestimo(livro2, joao, LocalDate.of(2026, 6, 1));
        } catch (RuntimeException e) {
            System.out.println("\nFalha esperada ao tentar emprestar livro indisponível: " + e.getMessage());
        }

        // 5) Devolução em atraso -> cálculo de multa
        double multa = emprestimoService.registrarDevolucao(emp1.getId(), LocalDate.of(2026, 6, 12));
        System.out.printf("%nDevolução registrada. Dias de atraso cobrados via CalculadoraMulta. Multa = R$ %.2f%n", multa);
        System.out.println("Livro após devolução: " + livro1);

        System.out.println("\n=== FIM DO TESTE MANUAL - todas as operações executaram como esperado ===");
    }
}
