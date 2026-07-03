package br.com.biblioteca.model;

import java.time.LocalDate;

/**
 * Entidade que representa um empréstimo de um exemplar de Livro para um Membro.
 * Responsabilidade única: manter o estado do empréstimo (datas, status),
 * sem calcular multa (isso é responsabilidade de CalculadoraMulta) e sem
 * acessar banco de dados (isso é responsabilidade dos Repository).
 */
public class Emprestimo {

    private Long id;
    private Livro livro;
    private Membro membro;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucaoEfetiva;
    private StatusEmprestimo status;

    public Emprestimo(Long id, Livro livro, Membro membro, LocalDate dataEmprestimo, int diasParaDevolucao) {
        this.id = id;
        this.livro = livro;
        this.membro = membro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataEmprestimo.plusDays(diasParaDevolucao);
        this.status = StatusEmprestimo.ATIVO;
    }

    public void registrarDevolucao(LocalDate dataDevolucao) {
        if (status == StatusEmprestimo.DEVOLVIDO) {
            throw new IllegalStateException("Empréstimo já foi devolvido.");
        }
        this.dataDevolucaoEfetiva = dataDevolucao;
        this.status = StatusEmprestimo.DEVOLVIDO;
    }

    /**
     * Reconstrói um Emprestimo a partir de dados já persistidos (usado pelos
     * Repository ao ler do banco), preservando o construtor "de negócio"
     * acima para quando um NOVO empréstimo está sendo criado. Mantém a
     * responsabilidade de reidratação separada da responsabilidade de criar
     * um empréstimo novo (que recalcula a data prevista de devolução).
     */
    public static Emprestimo reconstituir(Long id, Livro livro, Membro membro, LocalDate dataEmprestimo,
                                           LocalDate dataPrevistaDevolucao, LocalDate dataDevolucaoEfetiva,
                                           StatusEmprestimo status) {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.id = id;
        emprestimo.livro = livro;
        emprestimo.membro = membro;
        emprestimo.dataEmprestimo = dataEmprestimo;
        emprestimo.dataPrevistaDevolucao = dataPrevistaDevolucao;
        emprestimo.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
        emprestimo.status = status;
        return emprestimo;
    }

    private Emprestimo() {
        // usado apenas por reconstituir()
    }

    public boolean isAtrasado(LocalDate dataReferencia) {
        LocalDate limite = dataDevolucaoEfetiva != null ? dataDevolucaoEfetiva : dataReferencia;
        return limite.isAfter(dataPrevistaDevolucao);
    }

    public long getDiasAtraso(LocalDate dataReferencia) {
        LocalDate limite = dataDevolucaoEfetiva != null ? dataDevolucaoEfetiva : dataReferencia;
        if (!limite.isAfter(dataPrevistaDevolucao)) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dataPrevistaDevolucao, limite);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Livro getLivro() { return livro; }
    public Membro getMembro() { return membro; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public LocalDate getDataDevolucaoEfetiva() { return dataDevolucaoEfetiva; }
    public StatusEmprestimo getStatus() { return status; }

    @Override
    public String toString() {
        return String.format("Emprestimo{id=%d, livro='%s', membro='%s', status=%s, previsto=%s}",
                id, livro.getTitulo(), membro.getNome(), status, dataPrevistaDevolucao);
    }
}
