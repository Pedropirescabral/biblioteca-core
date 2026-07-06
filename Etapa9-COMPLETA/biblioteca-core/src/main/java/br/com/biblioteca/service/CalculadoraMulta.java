package br.com.biblioteca.service;

import br.com.biblioteca.model.Emprestimo;

/**
 * Estratégia de cálculo de multa por atraso (padrão Strategy).
 * Antes, esse cálculo estava espalhado e duplicado em métodos de tela do
 * projeto desktop (ex.: TelaEmprestimo e TelaDevolucao repetiam a mesma
 * fórmula). Isolar essa regra em uma classe própria elimina a duplicação
 * (code smell "Duplicated Code") e permite trocar a fórmula de cálculo no
 * futuro (ex.: multa progressiva) sem alterar quem a utiliza (OCP).
 */
public class CalculadoraMulta {

    private static final double VALOR_MULTA_POR_DIA = 0.50; // antes era um "magic number" espalhado pelo código

    public double calcular(Emprestimo emprestimo, java.time.LocalDate dataReferencia) {
        long diasAtraso = emprestimo.getDiasAtraso(dataReferencia);
        return diasAtraso * VALOR_MULTA_POR_DIA;
    }
}
