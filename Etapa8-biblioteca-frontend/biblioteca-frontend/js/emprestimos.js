/**
 * emprestimos.js — lógica da página emprestimos.html
 */
function preencherSelects() {
  const selectLivro = document.getElementById("livro");
  const selectMembro = document.getElementById("membro");

  const livrosDisponiveis = BibliotecaDados.getLivros().filter(l => l.disponiveis > 0);
  selectLivro.innerHTML = livrosDisponiveis.length
    ? livrosDisponiveis.map(l => `<option value="${l.id}">${l.titulo} (${l.disponiveis} disp.)</option>`).join("")
    : `<option value="">Nenhum livro disponível</option>`;

  const membros = BibliotecaDados.getMembros();
  selectMembro.innerHTML = membros.map(m => `<option value="${m.id}">${m.nome} — ${m.matricula}</option>`).join("");
}

function renderizarTabelaEmprestimos() {
  const corpo = document.getElementById("tabela-emprestimos");
  const emprestimos = BibliotecaDados.getEmprestimos();
  corpo.innerHTML = "";

  if (emprestimos.length === 0) {
    corpo.innerHTML = `<tr><td colspan="6">Nenhum empréstimo registrado.</td></tr>`;
    return;
  }

  emprestimos.slice().reverse().forEach(emp => {
    const livro = BibliotecaDados.buscarLivro(emp.livroId);
    const membro = BibliotecaDados.buscarMembro(emp.membroId);
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${livro ? livro.titulo : "—"}</td>
      <td>${membro ? membro.nome : "—"}</td>
      <td>${emp.dataEmprestimo}</td>
      <td>${emp.dataPrevista}</td>
      <td><span class="selo ${emp.status === 'ATIVO' ? 'ativo' : 'devolvido'}">${emp.status === 'ATIVO' ? 'Ativo' : 'Devolvido'}</span></td>
      <td>${emp.status === 'ATIVO' ? `<button class="btn secundario" data-id="${emp.id}">Registrar devolução</button>` : '—'}</td>
    `;
    corpo.appendChild(tr);
  });

  corpo.querySelectorAll("button[data-id]").forEach(botao => {
    botao.addEventListener("click", () => {
      BibliotecaDados.registrarDevolucao(Number(botao.dataset.id));
      preencherSelects();
      renderizarTabelaEmprestimos();
      Validacao.mostrarMensagem("msg-sucesso", "Devolução registrada com sucesso.", "sucesso");
    });
  });
}

document.addEventListener("DOMContentLoaded", () => {
  const hoje = new Date().toISOString().slice(0, 10);
  document.getElementById("data-emprestimo").value = hoje;

  preencherSelects();
  renderizarTabelaEmprestimos();

  const form = document.getElementById("form-emprestimo");
  form.addEventListener("submit", (evento) => {
    evento.preventDefault();

    const livroId = document.getElementById("livro").value;
    const membroId = document.getElementById("membro").value;
    const data = document.getElementById("data-emprestimo").value;

    Validacao.limparErros(["livro", "membro", "data"]);
    let valido = true;

    if (!Validacao.obrigatorio(livroId)) {
      Validacao.exibirErro("livro", "Selecione um livro disponível.");
      valido = false;
    }
    if (!Validacao.obrigatorio(membroId)) {
      Validacao.exibirErro("membro", "Selecione um membro.");
      valido = false;
    }
    if (!Validacao.obrigatorio(data)) {
      Validacao.exibirErro("data", "Informe a data do empréstimo.");
      valido = false;
    }

    if (!valido) return;

    try {
      BibliotecaDados.adicionarEmprestimo({ livroId, membroId, dataEmprestimo: data });
      preencherSelects();
      renderizarTabelaEmprestimos();
      Validacao.mostrarMensagem("msg-sucesso", "Empréstimo registrado com sucesso.", "sucesso");
      form.reset();
      document.getElementById("data-emprestimo").value = hoje;
    } catch (erro) {
      Validacao.mostrarMensagem("msg-erro", erro.message, "alerta");
    }
  });
});
