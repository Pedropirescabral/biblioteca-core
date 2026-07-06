package br.com.biblioteca.model;

/**
 * Entidade que representa um livro do acervo.
 * Responsabilidade única: manter o estado e as regras intrínsecas de um Livro
 * (ex.: saber se está disponível). Não conhece banco de dados nem interface gráfica.
 */
public class Livro {

    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private int quantidadeTotal;
    private int quantidadeDisponivel;

    public Livro(Long id, String titulo, String autor, String isbn, int quantidadeTotal) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Título do livro não pode ser vazio.");
        }
        if (quantidadeTotal < 0) {
            throw new IllegalArgumentException("Quantidade total não pode ser negativa.");
        }
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeTotal;
    }

    public boolean isDisponivel() {
        return quantidadeDisponivel > 0;
    }

    public void reservarExemplar() {
        if (!isDisponivel()) {
            throw new IllegalStateException("Não há exemplares disponíveis para reserva.");
        }
        quantidadeDisponivel--;
    }

    public void devolverExemplar() {
        if (quantidadeDisponivel >= quantidadeTotal) {
            throw new IllegalStateException("Quantidade disponível não pode exceder o total.");
        }
        quantidadeDisponivel++;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public int getQuantidadeTotal() { return quantidadeTotal; }
    public int getQuantidadeDisponivel() { return quantidadeDisponivel; }

    @Override
    public String toString() {
        return String.format("Livro{id=%d, titulo='%s', autor='%s', disponivel=%d/%d}",
                id, titulo, autor, quantidadeDisponivel, quantidadeTotal);
    }
}
