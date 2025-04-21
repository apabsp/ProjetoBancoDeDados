import React, { useState } from 'react';

export default function ObraForm() {
  const [titulo, setTitulo] = useState('');
  const [ano, setAno] = useState('');
  const [genero, setGenero] = useState('');    // ← novo estado
  const [msg, setMsg] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg('');

    try {
      // monta o payload incluindo genero só se houver algo digitado
      const payload = { titulo, ano };
      if (genero.trim()) payload.genero = genero.trim();

      const res = await fetch('http://localhost:8080/api/obra/inserir', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      const text = await res.text();
      if (!res.ok) throw new Error(text);

      setMsg(`✅ ${text}`);
      setTitulo(''); 
      setAno(''); 
      setGenero('');      // limpa o campo
    } catch (e) {
      setMsg(`❌ ${e.message}`);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="bg-white p-4 rounded-lg shadow">
      <h3 className="font-semibold mb-2">Nova Obra</h3>

      <input
        className="w-full mb-2 p-2 border rounded"
        placeholder="Título"
        value={titulo}
        onChange={e => setTitulo(e.target.value)}
      />

      <input
        type="date"
        className="w-full mb-2 p-2 border rounded"
        value={ano}
        onChange={e => setAno(e.target.value)}
      />

      <input
        type="text"
        className="w-full mb-4 p-2 border rounded"
        placeholder="Gênero (opcional)"
        value={genero}
        onChange={e => setGenero(e.target.value)}
      />

      <button
        type="submit"
        className="w-full py-2 bg-green-500 text-white rounded hover:bg-green-600"
      >
        Inserir Obra
      </button>

      {msg && <p className="mt-2 text-sm">{msg}</p>}
    </form>
  );
}
