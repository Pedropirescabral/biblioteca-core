package br.com.biblioteca.web.repository;

import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.Membro;
import br.com.biblioteca.model.StatusEmprestimo;
import br.com.biblioteca.repository.EmprestimoRepository;
import br.com.biblioteca.repository.LivroRepository;
import br.com.biblioteca.repository.MembroRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Implementação JDBC de EmprestimoRepository. Depende das abstrações
 * LivroRepository e MembroRepository (não das implementações concretas)
 * para reidratar as entidades relacionadas de cada linha de empréstimo —
 * reforçando o DIP também dentro da própria camada web.
 */
@Repository
public class EmprestimoRepositoryJdbc implements EmprestimoRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LivroRepository livroRepository;
    private final MembroRepository membroRepository;

    public EmprestimoRepositoryJdbc(JdbcTemplate jdbcTemplate,
                                     LivroRepository livroRepository,
                                     MembroRepository membroRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.livroRepository = livroRepository;
        this.membroRepository = membroRepository;
    }

    private Emprestimo mapear(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Livro livro = livroRepository.buscarPorId(rs.getLong("livro_id"))
                .orElseThrow(() -> new IllegalStateException("Livro referenciado não encontrado"));
        Membro membro = membroRepository.buscarPorId(rs.getLong("membro_id"))
                .orElseThrow(() -> new IllegalStateException("Membro referenciado não encontrado"));

        Date dataDevolucaoEfetiva = rs.getDate("data_devolucao_efetiva");

        return Emprestimo.reconstituir(
                rs.getLong("id"),
                livro,
                membro,
                rs.getDate("data_emprestimo").toLocalDate(),
                rs.getDate("data_prevista_devolucao").toLocalDate(),
                dataDevolucaoEfetiva != null ? dataDevolucaoEfetiva.toLocalDate() : null,
                StatusEmprestimo.valueOf(rs.getString("status"))
        );
    }

    @Override
    public Emprestimo salvar(Emprestimo emprestimo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO emprestimo (livro_id, membro_id, data_emprestimo, data_prevista_devolucao, data_devolucao_efetiva, status) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, emprestimo.getLivro().getId());
            ps.setLong(2, emprestimo.getMembro().getId());
            ps.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            ps.setDate(4, Date.valueOf(emprestimo.getDataPrevistaDevolucao()));
            ps.setDate(5, emprestimo.getDataDevolucaoEfetiva() != null ? Date.valueOf(emprestimo.getDataDevolucaoEfetiva()) : null);
            ps.setString(6, emprestimo.getStatus().name());
            return ps;
        }, keyHolder);
        emprestimo.setId(keyHolder.getKey().longValue());
        return emprestimo;
    }

    @Override
    public Optional<Emprestimo> buscarPorId(Long id) {
        List<Emprestimo> resultado = jdbcTemplate.query("SELECT * FROM emprestimo WHERE id = ?", this::mapear, id);
        return resultado.stream().findFirst();
    }

    @Override
    public List<Emprestimo> listarTodos() {
        return jdbcTemplate.query("SELECT * FROM emprestimo ORDER BY data_emprestimo DESC", this::mapear);
    }

    @Override
    public List<Emprestimo> listarAtivosPorMembro(Long membroId) {
        return jdbcTemplate.query(
                "SELECT * FROM emprestimo WHERE membro_id = ? AND status = 'ATIVO'", this::mapear, membroId);
    }

    @Override
    public void atualizar(Emprestimo emprestimo) {
        jdbcTemplate.update(
                "UPDATE emprestimo SET data_devolucao_efetiva = ?, status = ? WHERE id = ?",
                emprestimo.getDataDevolucaoEfetiva() != null ? Date.valueOf(emprestimo.getDataDevolucaoEfetiva()) : null,
                emprestimo.getStatus().name(),
                emprestimo.getId());
    }
}
