INSERT INTO livro (titulo, autor, isbn, quantidade_total, quantidade_disponivel) VALUES
 ('Clean Code', 'Robert C. Martin', '978-0132350884', 2, 1),
 ('Domain-Driven Design', 'Eric Evans', '978-0321125217', 1, 0),
 ('Refactoring', 'Martin Fowler', '978-0134757599', 3, 3);

INSERT INTO membro (nome, email, matricula) VALUES
 ('João Silva', 'joao@email.com', 'M001'),
 ('Maria Souza', 'maria@email.com', 'M002');

INSERT INTO emprestimo (livro_id, membro_id, data_emprestimo, data_prevista_devolucao, data_devolucao_efetiva, status) VALUES
 (2, 1, '2026-06-20', '2026-06-27', NULL, 'ATIVO');
