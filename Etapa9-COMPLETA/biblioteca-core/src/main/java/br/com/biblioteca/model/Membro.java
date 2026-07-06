package br.com.biblioteca.model;

/**
 * Entidade que representa um membro (usuário) da biblioteca.
 */
public class Membro {

    private Long id;
    private String nome;
    private String email;
    private String matricula;

    public Membro(Long id, String nome, String email, String matricula) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do membro não pode ser vazio.");
        }
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getMatricula() { return matricula; }

    @Override
    public String toString() {
        return String.format("Membro{id=%d, nome='%s', matricula='%s'}", id, nome, matricula);
    }
}
