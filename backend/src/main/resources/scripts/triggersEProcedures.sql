CREATE PROCEDURE dados_cliente (idCliente VARCHAR(100))
	SELECT p.nome FROM cliente c
	JOIN pessoa p ON c.fk_Pessoa_id = p.id
	WHERE idCliente = c.fk_Pessoa_id;

DROP PROCEDURE dados_cliente;

DELIMITER //

CREATE FUNCTION dias_ate_devolucao(idEmprestimo VARCHAR(50)) RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE dias INT;
    SELECT DATEDIFF(DATE(data_prevista_dev), CURDATE())
    INTO dias
    FROM Emprestimo_aluga
    WHERE id = idEmprestimo;
    RETURN dias;
END

DELIMITER ;


CREATE TRIGGER verificar_exemplar_disponivel
BEFORE INSERT ON Emprestimo_aluga
FOR EACH ROW
BEGIN
	DECLARE exemplar_em_uso int DEFAULT 0;

	-- CHECK AQUI PRA VER SE O ITEM JÁ FOI RETORNADO
	SELECT count(*) INTO exemplar_em_uso
	FROM emprestimo_aluga ea
	WHERE fk_exemplar = NEW.fk_exemplar
	AND data_devolucao IS NULL;

	IF exemplar_em_uso > 0 THEN
		SIGNAL SQLSTATE '45000'  -- This IS how you SIGNAL an error apparently.
		SET MESSAGE_TEXT = "Exemplar já está emprestado e não devolvido.";
	END IF;

END