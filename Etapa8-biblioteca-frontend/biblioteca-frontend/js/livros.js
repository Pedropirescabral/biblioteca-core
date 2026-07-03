/**
 * livros.js — lógica da página livros.html
 */
function renderizarTabelaLivros() {
  const corpo = document.getElementById("tabela-livros");
  const livros = BibliotecaDados.getLivros();
  corpo.innerHTML = "";

  if (livros.length === 0) {
    corpo.innerHTML = `<tr><td colspan="5">Nenhum livro cadastrado.</td></tr>`;
    return;
  }

  livros.forEach(l => {
    const disponivel = l.disponiveis > 0;
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${l.titulo}</td>
      <td>${l.autor}</td>
      <td>${l.isbn || "—"}</td>
      <td>${l.disponiveis}/${l.total}</td>
      <td><span class="selo ${disponivel ? 'disponivel' : 'indisponivel'}">${disponivel ? 'Disponível' : 'Indisponível'}</span></td>
    `;
    corpo.appendChild(tr);
  });
}

document.addEventListener("DOMContentLoaded", () => {
  renderizarTabelaLivros();

  const form = document.getElementById("form-livro");
  form.addEventListener("submit", (evento) => {
    evento.preventDefault();

    const titulo = document.getElementById("titulo").value;
    const autor = document.getElementById("autor").value;
    const isbn = document.getElementById("isbn").value;
    const quantidade = document.getElementById("quantidade").value;

    Validacao.limparErros(["titulo", "autor", "isbn", "quantidade"]);
    let valido = true;

    if (!Validacao.tamanhoMinimo(titulo, 2)) {
      Validacao.exibirErro("titulo", "Informe um título com ao menos 2 caracteres.");
      valido = false;
    }
    if (!Validacao.tamanhoMinimo(autor, 2)) {
      Validacao.exibirErro("autor", "Informe o nome do autor.");
      valido = false;
    }
    if (!Validacao.numeroPositivo(quantidade)) {
      Validacao.exibirErro("quantidade", "Informe uma quantidade válida (maior que zero).");
      valido = false;
    }

    if (!valido) return;

    BibliotecaDados.adicionarLivro({
      titulo: titulo.trim(),
      autor: autor.trim(),
      isbn: isbn.trim(),
      quantidade: Number(quantidade),
    });

    form.reset();
    document.getElementById("quantidade").value = 1;
    renderizarTabelaLivros();
    Validacao.mostrarMensagem("msg-sucesso", `Livro "${titulo}" cadastrado com sucesso.`, "sucesso");
  });
});
