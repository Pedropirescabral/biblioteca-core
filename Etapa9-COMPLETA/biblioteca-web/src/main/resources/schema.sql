DROP TABLE IF EXISTS emprestimo;
DROP TABLE IF EXISTS livro;
DROP TABLE IF EXISTS membro;

CREATE TABLE livro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(150),
    isbn VARCHAR(30),
    quantidade_total INT NOT NULL,
    quantidade_disponivel INT NOT NULL
);

CREATE TABLE membro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150),
    matricula VARCHAR(30)
);

CREATE TABLE emprestimo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    livro_id BIGINT NOT NULL REFERENCES livro(id),
    membro_id BIGINT NOT NULL REFERENCES membro(id),
    data_emprestimo DATE NOT NULL,
    data_prevista_devolucao DATE NOT NULL,
    data_devolucao_efetiva DATE,
    status VARCHAR(20) NOT NULL
);
