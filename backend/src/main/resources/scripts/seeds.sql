-- Switch to your database
USE biblioteca;

-- 1) Independent tables
CREATE TABLE IF NOT EXISTS Pessoa (
    id VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100),
    complemento VARCHAR(100),
    cep VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Editora (
    id VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Autor (
    id VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Genero (
    nome VARCHAR(50) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS Estante (
    prateleira VARCHAR(50),
    numero VARCHAR(50),
    PRIMARY KEY (prateleira, numero)
);

-- 2) Dependent tables
CREATE TABLE IF NOT EXISTS Obra (
    cod_barras VARCHAR(50) PRIMARY KEY DEFAULT (UUID()),
    titulo     VARCHAR(100) NOT NULL,
    ano_lanc   DATE,
    genero     VARCHAR(50),
    FOREIGN KEY (genero) REFERENCES Genero(nome)
);

CREATE TABLE IF NOT EXISTS Cliente (
    id VARCHAR(50) PRIMARY KEY,
    historico VARCHAR(100),
    fk_Pessoa_id VARCHAR(50),
    FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa(id)
);

CREATE TABLE IF NOT EXISTS Funcionario (
    id VARCHAR(50) PRIMARY KEY,
    fk_Pessoa_id VARCHAR(50),
    FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa(id)
);

CREATE TABLE IF NOT EXISTS Livro (
    fk_Obra_cod_barras VARCHAR(50) PRIMARY KEY,
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras)
);

-- 3) Artigo with its own id PK
CREATE TABLE IF NOT EXISTS Artigo (
    id VARCHAR(50) PRIMARY KEY,
    fk_Obra_cod_barras VARCHAR(50),
    titulo VARCHAR(100),
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras)
);

CREATE TABLE IF NOT EXISTS Edicao (
    id VARCHAR(50) PRIMARY KEY,
    livro_cod_barras VARCHAR(50),
    numero INT,
    FOREIGN KEY (livro_cod_barras) REFERENCES Livro(fk_Obra_cod_barras)
);

-- 4) Exemplar references composite Estante and new Artigo PK
CREATE TABLE IF NOT EXISTS Exemplar (
    id VARCHAR(50) PRIMARY KEY,
    fk_edicao VARCHAR(50),
    fk_artigo VARCHAR(50),
    fk_estante_prateleira VARCHAR(50),
    fk_estante_numero VARCHAR(50),
    CONSTRAINT FK_Exemplar_Edicao FOREIGN KEY (fk_edicao) REFERENCES Edicao(id),
    CONSTRAINT FK_Exemplar_Artigo FOREIGN KEY (fk_artigo) REFERENCES Artigo(id),
    CONSTRAINT FK_Exemplar_Estante FOREIGN KEY (fk_estante_prateleira, fk_estante_numero)
      REFERENCES Estante(prateleira, numero)
);

-- 5) Join tables
CREATE TABLE IF NOT EXISTS publica (
    fk_Obra_cod_barras VARCHAR(50),
    fk_Editora_id      VARCHAR(50),
    PRIMARY KEY (fk_Obra_cod_barras, fk_Editora_id),
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras),
    FOREIGN KEY (fk_Editora_id)      REFERENCES Editora(id)
);

CREATE TABLE IF NOT EXISTS escreve (
    fk_Autor_id          VARCHAR(50),
    fk_Obra_cod_barras   VARCHAR(50),
    PRIMARY KEY (fk_Autor_id, fk_Obra_cod_barras),
    FOREIGN KEY (fk_Autor_id)        REFERENCES Autor(id),
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras)
);

CREATE TABLE IF NOT EXISTS Pertence (
    fk_Genero_nome      VARCHAR(50),
    fk_Obra_cod_barras  VARCHAR(50),
    PRIMARY KEY (fk_Genero_nome, fk_Obra_cod_barras),
    FOREIGN KEY (fk_Genero_nome)     REFERENCES Genero(nome),
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras)
);

-- 6) Telefone
CREATE TABLE IF NOT EXISTS Telefone (
    telefone_pk VARCHAR(50) PRIMARY KEY,
    numero      INT,
    fk_Pessoa_id VARCHAR(50),
    FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa(id)
);

-- 7) Emprestimo_aluga
CREATE TABLE IF NOT EXISTS Emprestimo_aluga (
    id                 VARCHAR(50) PRIMARY KEY,
    hora               TIME        NOT NULL,
    data_prevista_dev  DATETIME,
    data_devolucao     DATE,
    data_emprestimo    DATE,
    fk_exemplar        VARCHAR(50),
    fk_cliente         VARCHAR(50),
    CONSTRAINT FK_Emprestimo_Exemplar FOREIGN KEY (fk_exemplar) REFERENCES Exemplar(id),
    CONSTRAINT FK_Emprestimo_Cliente  FOREIGN KEY (fk_cliente)  REFERENCES Cliente(id)
);

-- Seed data
-- 1) Pessoas and Clientes
INSERT IGNORE INTO Pessoa (id, nome, complemento, cep) VALUES
  ('p1', 'João Silva', 'Apto 101', '50000-000'),
  ('p2', 'Maria Oliveira', 'Casa 22', '51000-100'),
  ('p3', 'Carlos Souza', NULL,        '52000-200');

INSERT IGNORE INTO Cliente (id, historico, fk_Pessoa_id) VALUES
  ('c1', 'Frequent borrower', 'p1'),
  ('c2', 'Paid late once',    'p2'),
  ('c3', 'New customer',      'p3');

-- 2) Estantes
INSERT IGNORE INTO Estante (prateleira, numero) VALUES
  ('A', '01'),
  ('A', '02'),
  ('B', '01');

-- 3) Obra, Livro, Artigo, Edicao
INSERT IGNORE INTO Obra (cod_barras, titulo, ano_lanc, genero) VALUES
  ('obra1', 'Exemplum Livro', '2020-05-15', NULL);

INSERT IGNORE INTO Livro (fk_Obra_cod_barras) VALUES
  ('obra1');

INSERT IGNORE INTO Artigo (id, fk_Obra_cod_barras, titulo) VALUES
  ('art1', 'obra1', 'Artigo de Exemplo');

INSERT IGNORE INTO Edicao (id, livro_cod_barras, numero) VALUES
  ('ed1', 'obra1', 1);

-- 4) Exemplares
INSERT IGNORE INTO Exemplar (
  id,
  fk_edicao,
  fk_artigo,
  fk_estante_prateleira,
  fk_estante_numero
) VALUES
  ('ex1', 'ed1', 'art1', 'A', '01'),
  ('ex2', 'ed1', 'art1', 'A', '02'),
  ('ex3', 'ed1', 'art1', 'B', '01');
