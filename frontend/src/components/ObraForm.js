import React, { useState } from 'react';

function ObraForm() {
  const [codBarras, setCodBarras] = useState('');
  const [titulo, setTitulo] = useState('');
  const [anoLanc, setAnoLanc] = useState('');
  const [genero, setGenero] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    const obra = {
      codBarras,
      titulo,
      anoLanc,
      genero
    };

    try {
      const response = await fetch('http://localhost:8080/api/obra/inserir', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(obra)
      });

      if (!response.ok) throw new Error('Erro ao inserir obra');

      const result = await response.text();
      setMessage(`Sucesso: ${result}`);
    } catch (err) {
      setMessage(`Erro: ${err.message}`);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>
        Código de Barras:
        <input type="text" value={codBarras} onChange={(e) => setCodBarras(e.target.value)} required />
      </label>
      <br />
      <label>
        Título:
        <input type="text" value={titulo} onChange={(e) => setTitulo(e.target.value)} required />
      </label>
      <br />
      <label>
        Ano de Lançamento:
        <input type="text" value={anoLanc} onChange={(e) => setAnoLanc(e.target.value)} required />
      </label>
      <br />
      <label>
        Gênero:
        <input type="text" value={genero} onChange={(e) => setGenero(e.target.value)} required />
      </label>
      <br />
      <button type="submit">📥 Cadastrar Obra</button>
      <p>{message}</p>
    </form>
  );
}

export default ObraForm;
