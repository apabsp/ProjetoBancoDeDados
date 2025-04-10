INSERT INTO Obra (cod_barras, titulo, ano_lanc)
VALUES (UUID(), '${titulo}', '${ano}');

INSERT INTO Pertence (fk_Genero_nome, fk_Obra_cod_barras)
VALUES ('${genero}', (SELECT cod_barras FROM Obra WHERE titulo = '${titulo}' AND ano_lanc = '${ano}'));
