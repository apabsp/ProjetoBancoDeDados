ALTER TABLE Obra
  ADD COLUMN genero VARCHAR(50) NULL,
  ADD CONSTRAINT FK_Obra_Genero
    FOREIGN KEY (genero)
    REFERENCES Genero(nome);
