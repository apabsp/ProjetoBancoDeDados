import React, { useState } from 'react';

function EditoraForm() {
  const [id, setId] = useState('');
  const [nome, setNome] = useState('');
  const [mensagem, setMensagem] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    const editora = { id, nome };

    try {
      const response = await fetch('http://localhost:8080/editoras', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(editora),
      });

      if (response.ok) {
        setMensagem('Editora adicionada com sucesso!');
        setId('');
        setNome('');
      } else {
        setMensagem('Erro ao adicionar editora.');
      }
    } catch (error) {
      console.error('Erro ao conectar com o backend:', error);
      setMensagem('Erro ao conectar com o backend.');
    }
  };

  return (
    <div>
      <h2>Cadastrar Editora</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>ID:</label>
          <input
            type="text"
            value={id}
            onChange={(e) => setId(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Nome:</label>
          <input
            type="text"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
          />
        </div>
        <button type="submit">Salvar</button>
      </form>
      {mensagem && <p>{mensagem}</p>}
    </div>
  );
}

export default EditoraForm;
