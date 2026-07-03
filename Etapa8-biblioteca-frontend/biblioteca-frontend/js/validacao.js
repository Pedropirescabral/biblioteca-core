/**
 * validacao.js
 * Funções genéricas de validação de formulário, reutilizadas pelas telas
 * de livros, membros e empréstimos.
 */
const Validacao = {
  exibirErro(idCampo, mensagem) {
    const el = document.getElementById(`erro-${idCampo}`);
    if (el) el.textContent = mensagem || "";
  },

  limparErros(idsCampos) {
    idsCampos.forEach(id => this.exibirErro(id, ""));
  },

  obrigatorio(valor) {
    return valor !== null && valor.trim().length > 0;
  },

  tamanhoMinimo(valor, minimo) {
    return valor.trim().length >= minimo;
  },

  emailValido(valor) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(valor.trim());
  },

  numeroPositivo(valor) {
    const n = Number(valor);
    return Number.isFinite(n) && n > 0;
  },

  mostrarMensagem(idMensagem, texto, tipo = "sucesso") {
    const el = document.getElementById(idMensagem);
    if (!el) return;
    el.textContent = texto;
    el.className = `mensagem ${tipo}`;
    el.hidden = false;
    setTimeout(() => { el.hidden = true; }, 4000);
  },
};
