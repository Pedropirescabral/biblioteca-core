package br.com.biblioteca.exception;

/** Lançada quando se tenta emprestar um livro sem exemplares disponíveis. */
public class LivroIndisponivelException extends RuntimeException {
    public LivroIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
