/**
 * painel.js — lógica da página index.html (dashboard)
 */
document.addEventListener("DOMContentLoaded", () => {
  const livros = BibliotecaDados.getLivros();
  const membros = BibliotecaDados.getMembros();
  const emprestimos = BibliotecaDados.getEmprestimos();

  document.getElementById("total-livros").textContent = livros.length;
  document.getElementById("total-disponiveis").textContent =
    livros.reduce((soma, l) => soma + l.disponiveis, 0);
  document.getElementById("total-membros").textContent = membros.length;
  document.getElementById("total-emprestimos").textContent =
    emprestimos.filter(e => e.status === "ATIVO").length;

  const corpoTabela = document.getElementById("tabela-recentes");
  corpoTabela.innerHTML = "";

  if (emprestimos.length === 0) {
    corpoTabela.innerHTML = `<tr><td colspan="4">Nenhum empréstimo registrado ainda.</td></tr>`;
    return;
  }

  emprestimos.slice().reverse().forEach(emp => {
    const livro = BibliotecaDados.buscarLivro(emp.livroId);
    const membro = BibliotecaDados.buscarMembro(emp.membroId);
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${livro ? livro.titulo : "—"}</td>
      <td>${membro ? membro.nome : "—"}</td>
      <td>${emp.dataPrevista}</td>
      <td><span class="selo ${emp.status === 'ATIVO' ? 'ativo' : 'devolvido'}">${emp.status === 'ATIVO' ? 'Ativo' : 'Devolvido'}</span></td>
    `;
    corpoTabela.appendChild(tr);
  });
});
