/**
 * dados.js
 * Camada de dados simulada para o protótipo de front-end (Etapa 8).
 * Nesta etapa não há back-end (conforme o enunciado), então os dados vivem
 * em memória (variável global) e são reiniciados a cada carregamento de
 * página. Na Etapa 9, essas mesmas telas passam a consumir o back-end
 * Spring (JDBC) no lugar deste arquivo.
 */
const BibliotecaDados = (function () {
  let livros = [
    { id: 1, titulo: "Clean Code", autor: "Robert C. Martin", isbn: "978-0132350884", total: 2, disponiveis: 1 },
    { id: 2, titulo: "Domain-Driven Design", autor: "Eric Evans", isbn: "978-0321125217", total: 1, disponiveis: 0 },
    { id: 3, titulo: "Refactoring", autor: "Martin Fowler", isbn: "978-0134757599", total: 3, disponiveis: 3 },
  ];

  let membros = [
    { id: 1, nome: "João Silva", email: "joao@email.com", matricula: "M001" },
    { id: 2, nome: "Maria Souza", email: "maria@email.com", matricula: "M002" },
  ];

  let emprestimos = [
    { id: 1, livroId: 2, membroId: 1, dataEmprestimo: "2026-06-20", dataPrevista: "2026-06-27", status: "ATIVO" },
  ];

  let proximoLivroId = 4;
  let proximoMembroId = 3;
  let proximoEmprestimoId = 2;

  return {
    getLivros: () => livros,
    getMembros: () => membros,
    getEmprestimos: () => emprestimos,

    adicionarLivro(livro) {
      const novo = { id: proximoLivroId++, disponiveis: livro.quantidade, total: livro.quantidade, ...livro };
      livros.push(novo);
      return novo;
    },

    adicionarMembro(membro) {
      const novo = { id: proximoMembroId++, ...membro };
      membros.push(novo);
      return novo;
    },

    adicionarEmprestimo({ livroId, membroId, dataEmprestimo }) {
      const livro = livros.find(l => l.id === Number(livroId));
      if (!livro || livro.disponiveis <= 0) {
        throw new Error("Livro indisponível para empréstimo.");
      }
      livro.disponiveis -= 1;

      const dataPrevista = new Date(dataEmprestimo);
      dataPrevista.setDate(dataPrevista.getDate() + 7);

      const novo = {
        id: proximoEmprestimoId++,
        livroId: Number(livroId),
        membroId: Number(membroId),
        dataEmprestimo,
        dataPrevista: dataPrevista.toISOString().slice(0, 10),
        status: "ATIVO",
      };
      emprestimos.push(novo);
      return novo;
    },

    registrarDevolucao(emprestimoId) {
      const emp = emprestimos.find(e => e.id === emprestimoId);
      if (!emp || emp.status === "DEVOLVIDO") return;
      emp.status = "DEVOLVIDO";
      const livro = livros.find(l => l.id === emp.livroId);
      if (livro) livro.disponiveis += 1;
    },

    buscarLivro: (id) => livros.find(l => l.id === Number(id)),
    buscarMembro: (id) => membros.find(m => m.id === Number(id)),
  };
})();
