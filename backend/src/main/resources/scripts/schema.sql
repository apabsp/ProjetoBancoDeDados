-- Tabelas independentes
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

-- Tabelas dependentes
CREATE TABLE IF NOT EXISTS Obra (
  cod_barras VARCHAR(50) PRIMARY KEY DEFAULT (UUID()),
  titulo VARCHAR(100) NOT NULL,
  ano_lanc DATE
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
    fk_Obra_cod_barras VARCHAR(50),
    PRIMARY KEY (fk_Obra_cod_barras),
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras)
);

CREATE TABLE IF NOT EXISTS Artigo (
    fk_Obra_cod_barras VARCHAR(50),
    titulo VARCHAR(100),
    PRIMARY KEY (fk_Obra_cod_barras),
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras)
);

CREATE TABLE IF NOT EXISTS Emprestimo_aluga (
    id VARCHAR(50) PRIMARY KEY,
    hora TIME,
    data_prevista_dev DATE,
    data_devolucao DATE,
    fk_Cliente_id VARCHAR(50),
    FOREIGN KEY (fk_Cliente_id) REFERENCES Cliente(id)
);

CREATE TABLE IF NOT EXISTS Telefone (
    telefone_pk VARCHAR(50) PRIMARY KEY,
    numero INT,
    fk_Pessoa_id VARCHAR(50),
    FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa(id)
);

-- Tabelas de junção
CREATE TABLE IF NOT EXISTS publica (
    fk_Obra_cod_barras VARCHAR(50),
    fk_Editora_id VARCHAR(50),
    PRIMARY KEY (fk_Obra_cod_barras, fk_Editora_id),
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras),
    FOREIGN KEY (fk_Editora_id) REFERENCES Editora(id)
);

CREATE TABLE IF NOT EXISTS escreve (
    fk_Autor_id VARCHAR(50),
    fk_Obra_cod_barras VARCHAR(50),
    PRIMARY KEY (fk_Autor_id, fk_Obra_cod_barras),
    FOREIGN KEY (fk_Autor_id) REFERENCES Autor(id),
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras)
);

CREATE TABLE IF NOT EXISTS Pertence (
    fk_Genero_nome VARCHAR(50),
    fk_Obra_cod_barras VARCHAR(50),
    PRIMARY KEY (fk_Genero_nome, fk_Obra_cod_barras),
    FOREIGN KEY (fk_Genero_nome) REFERENCES Genero(nome),
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras)
);

CREATE TABLE IF NOT EXISTS Edicao (
    id VARCHAR(50) PRIMARY KEY,
    livro_cod_barras VARCHAR(50),
    numero INT,
    FOREIGN KEY (livro_cod_barras) REFERENCES Livro(fk_Obra_cod_barras)
);
