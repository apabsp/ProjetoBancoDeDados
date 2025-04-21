import React, { useState } from 'react';

function ObraInsertForm() {
  const [titulo, setTitulo] = useState('');
  const [ano, setAno] = useState('');
  const [genero, setGenero] = useState('');      
  const [message, setMessage] = useState('');

  const handleSubmit = async () => {
    try {
      const payload = { titulo, ano };
      if (genero.trim()) payload.genero = genero.trim();

      const response = await fetch('http://localhost:8080/api/obra/inserir', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      const text = await response.text();
      if (!response.ok) throw new Error(text);

      setMessage(`✅ ${text}`);
      setTitulo(''); setAno(''); setGenero(''); 
    } catch (err) {
      setMessage(`❌ ${err.message}`);
    }
  };

  return (
    <div>
      <h4>Inserir nova obra</h4>

      <input
        type="text"
        placeholder="Título"
        value={titulo}
        onChange={e => setTitulo(e.target.value)}
      />

      <input
        type="date"
        placeholder="Ano de Lançamento"
        value={ano}
        onChange={e => setAno(e.target.value)}
      />

      <input
        type="text"
        placeholder="Gênero (opcional)"
        value={genero}
        onChange={e => setGenero(e.target.value)}
      />

      <button onClick={handleSubmit}>➕ Inserir Obra</button>
      <p>{message}</p>
    </div>
  );
}

export default ObraInsertForm;
