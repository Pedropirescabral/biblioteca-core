package br.com.biblioteca.web.repository;

import br.com.biblioteca.model.Livro;
import br.com.biblioteca.repository.LivroRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Implementação de LivroRepository usando Spring JdbcTemplate.
 *
 * Esta classe é a prova prática do Open/Closed e do Dependency Inversion
 * aplicados na Etapa 6: LivroService (em biblioteca-core) não foi alterado
 * em uma linha sequer para passar a usar banco de dados real — apenas esta
 * nova implementação da interface LivroRepository foi adicionada e
 * conectada via injeção de dependência (ver AppConfig).
 */
@Repository
public class LivroRepositoryJdbc implements LivroRepository {

    private final JdbcTemplate jdbcTemplate;

    public LivroRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Livro mapear(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Livro livro = new Livro(
                rs.getLong("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("isbn"),
                rs.getInt("quantidade_total")
        );
        int disponivel = rs.getInt("quantidade_disponivel");
        while (livro.getQuantidadeDisponivel() > disponivel) {
            livro.reservarExemplar();
        }
        return livro;
    }

    @Override
    public Livro salvar(Livro livro) {
        if (livro.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO livro (titulo, autor, isbn, quantidade_total, quantidade_disponivel) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, livro.getTitulo());
                ps.setString(2, livro.getAutor());
                ps.setString(3, livro.getIsbn());
                ps.setInt(4, livro.getQuantidadeTotal());
                ps.setInt(5, livro.getQuantidadeDisponivel());
                return ps;
            }, keyHolder);
            livro.setId(keyHolder.getKey().longValue());
        } else {
            atualizar(livro);
        }
        return livro;
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        List<Livro> resultado = jdbcTemplate.query(
                "SELECT * FROM livro WHERE id = ?", this::mapear, id);
        return resultado.stream().findFirst();
    }

    @Override
    public List<Livro> listarTodos() {
        return jdbcTemplate.query("SELECT * FROM livro ORDER BY titulo", this::mapear);
    }

    @Override
    public List<Livro> listarDisponiveis() {
        return jdbcTemplate.query(
                "SELECT * FROM livro WHERE quantidade_disponivel > 0 ORDER BY titulo", this::mapear);
    }

    @Override
    public void atualizar(Livro livro) {
        jdbcTemplate.update(
                "UPDATE livro SET titulo = ?, autor = ?, isbn = ?, quantidade_total = ?, quantidade_disponivel = ? WHERE id = ?",
                livro.getTitulo(), livro.getAutor(), livro.getIsbn(),
                livro.getQuantidadeTotal(), livro.getQuantidadeDisponivel(), livro.getId());
    }
}
