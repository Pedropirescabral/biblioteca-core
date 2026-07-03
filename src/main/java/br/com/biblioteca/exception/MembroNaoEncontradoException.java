package br.com.biblioteca.exception;

public class MembroNaoEncontradoException extends RuntimeException {
    public MembroNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
