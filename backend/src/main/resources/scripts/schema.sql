-- 1) Independent tables
CREATE TABLE IF NOT EXISTS Pessoa (
    id VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100),
    complemento VARCHAR(100),
    cep VARCHAR(50),
    numero INT
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
    cod_barras VARCHAR(50) PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    ano_lanc DATE
);

CREATE TABLE IF NOT EXISTS Telefone (
    telefone_pk VARCHAR(50) PRIMARY KEY,
    numero VARCHAR(20),
    fk_Pessoa_id VARCHAR(50),
    FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa(id)
);

ALTER TABLE Pessoa ADD COLUMN fk_telefone_telefone_pk VARCHAR(50);
ALTER TABLE Pessoa ADD FOREIGN KEY (fk_telefone_telefone_pk) REFERENCES Telefone(telefone_pk);

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

-- 3) Artigo
CREATE TABLE IF NOT EXISTS Artigo (
    id VARCHAR(50) PRIMARY KEY,
    fk_Obra_cod_barras VARCHAR(50),
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras)
);

CREATE TABLE IF NOT EXISTS Edicao (
    id VARCHAR(50) PRIMARY KEY,
    livro_cod_barras VARCHAR(50),
    nome INT,
    FOREIGN KEY (livro_cod_barras) REFERENCES Livro(fk_Obra_cod_barras)
);

-- 4) Exemplar references composite Estante and new Artigo PK
CREATE TABLE IF NOT EXISTS Exemplar (
    id VARCHAR(50) PRIMARY KEY,

    -- Original foreign keys
    fk_edicao VARCHAR(50),
    fk_artigo VARCHAR(50),

    -- Estante composite foreign key
    fk_estante_prateleira VARCHAR(50),
    fk_estante_numero VARCHAR(50),

    -- NEW: direct reference to the Obra
    fk_obra_cod_barras VARCHAR(50),

    -- Constraints
    CONSTRAINT FK_Exemplar_Edicao FOREIGN KEY (fk_edicao) REFERENCES Edicao(id),
    CONSTRAINT FK_Exemplar_Artigo FOREIGN KEY (fk_artigo) REFERENCES Artigo(id),
    CONSTRAINT FK_Exemplar_Estante FOREIGN KEY (fk_estante_prateleira, fk_estante_numero)
        REFERENCES Estante(prateleira, numero),
    CONSTRAINT FK_Exemplar_Obra FOREIGN KEY (fk_obra_cod_barras) REFERENCES Obra(cod_barras)
);

-- 5) Join tables
CREATE TABLE IF NOT EXISTS publica (
    fk_Obra_cod_barras VARCHAR(50),
    fk_Editora_id VARCHAR(50),
    PRIMARY KEY (fk_Obra_cod_barras, fk_Editora_id),
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras) ON DELETE CASCADE,
    FOREIGN KEY (fk_Editora_id) REFERENCES Editora(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS escreve (
    fk_Autor_id VARCHAR(50),
    fk_Obra_cod_barras VARCHAR(50),
    PRIMARY KEY (fk_Autor_id, fk_Obra_cod_barras),
    FOREIGN KEY (fk_Autor_id) REFERENCES Autor(id) ON DELETE CASCADE,
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Pertence (
    fk_Genero_nome VARCHAR(50),
    fk_Obra_cod_barras VARCHAR(50),
    PRIMARY KEY (fk_Genero_nome, fk_Obra_cod_barras),
    FOREIGN KEY (fk_Genero_nome) REFERENCES Genero(nome) ON DELETE CASCADE,
    FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras) ON DELETE CASCADE
);

-- 7) Emprestimo_aluga
CREATE TABLE IF NOT EXISTS Emprestimo_aluga (
    id VARCHAR(50) PRIMARY KEY,
    data_prevista_dev DATETIME,
    data_devolucao DATE,
    data_emprestimo DATETIME,
    fk_exemplar VARCHAR(50),
    fk_cliente VARCHAR(50),
    fk_funcionario VARCHAR(50),
    CONSTRAINT FK_Emprestimo_Exemplar FOREIGN KEY (fk_exemplar) REFERENCES Exemplar(id) ON DELETE CASCADE,
    CONSTRAINT FK_Emprestimo_Cliente FOREIGN KEY (fk_cliente) REFERENCES Cliente(id) ON DELETE CASCADE,
    CONSTRAINT FK_Emprestimo_Funcionario FOREIGN KEY (fk_funcionario) REFERENCES Funcionario(id) ON DELETE CASCADE
);