package br.com.biblioteca.web.repository;

import br.com.biblioteca.model.Membro;
import br.com.biblioteca.repository.MembroRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class MembroRepositoryJdbc implements MembroRepository {

    private final JdbcTemplate jdbcTemplate;

    public MembroRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Membro mapear(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new Membro(rs.getLong("id"), rs.getString("nome"), rs.getString("email"), rs.getString("matricula"));
    }

    @Override
    public Membro salvar(Membro membro) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO membro (nome, email, matricula) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, membro.getNome());
            ps.setString(2, membro.getEmail());
            ps.setString(3, membro.getMatricula());
            return ps;
        }, keyHolder);
        membro.setId(keyHolder.getKey().longValue());
        return membro;
    }

    @Override
    public Optional<Membro> buscarPorId(Long id) {
        List<Membro> resultado = jdbcTemplate.query("SELECT * FROM membro WHERE id = ?", this::mapear, id);
        return resultado.stream().findFirst();
    }

    @Override
    public List<Membro> listarTodos() {
        return jdbcTemplate.query("SELECT * FROM membro ORDER BY nome", this::mapear);
    }
}
