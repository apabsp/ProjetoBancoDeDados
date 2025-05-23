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