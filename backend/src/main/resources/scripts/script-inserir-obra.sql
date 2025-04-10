-- Inserir o gênero, caso não exista
INSERT IGNORE INTO Genero (nome) VALUES (:genero);

-- Inserir a obra
INSERT INTO Obra (cod_barras, titulo, ano_lanc)
VALUES (:cod_barras, :titulo, :ano_lanc);

-- Relacionar a obra ao gênero
INSERT INTO Pertence (fk_Genero_nome, fk_Obra_cod_barras)
VALUES (:genero, :cod_barras);
