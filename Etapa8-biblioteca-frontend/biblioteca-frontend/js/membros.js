/**
 * membros.js — lógica da página membros.html
 */
function renderizarTabelaMembros() {
  const corpo = document.getElementById("tabela-membros");
  const membros = BibliotecaDados.getMembros();
  const emprestimos = BibliotecaDados.getEmprestimos();
  corpo.innerHTML = "";

  if (membros.length === 0) {
    corpo.innerHTML = `<tr><td colspan="4">Nenhum membro cadastrado.</td></tr>`;
    return;
  }

  membros.forEach(m => {
    const ativos = emprestimos.filter(e => e.membroId === m.id && e.status === "ATIVO").length;
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${m.nome}</td>
      <td>${m.email}</td>
      <td>${m.matricula}</td>
      <td>${ativos}</td>
    `;
    corpo.appendChild(tr);
  });
}

document.addEventListener("DOMContentLoaded", () => {
  renderizarTabelaMembros();

  const form = document.getElementById("form-membro");
  form.addEventListener("submit", (evento) => {
    evento.preventDefault();

    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const matricula = document.getElementById("matricula").value;

    Validacao.limparErros(["nome", "email", "matricula"]);
    let valido = true;

    if (!Validacao.tamanhoMinimo(nome, 3)) {
      Validacao.exibirErro("nome", "Informe o nome completo (mínimo 3 caracteres).");
      valido = false;
    }
    if (!Validacao.emailValido(email)) {
      Validacao.exibirErro("email", "Informe um e-mail válido.");
      valido = false;
    }
    if (!Validacao.obrigatorio(matricula)) {
      Validacao.exibirErro("matricula", "Informe a matrícula do membro.");
      valido = false;
    }

    if (!valido) return;

    BibliotecaDados.adicionarMembro({
      nome: nome.trim(),
      email: email.trim(),
      matricula: matricula.trim(),
    });

    form.reset();
    renderizarTabelaMembros();
    Validacao.mostrarMensagem("msg-sucesso", `Membro "${nome}" cadastrado com sucesso.`, "sucesso");
  });
});
