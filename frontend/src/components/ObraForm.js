import React, { useState } from 'react';

export default function ObraForm() {
  const [titulo, setTitulo] = useState('');
  const [ano, setAno] = useState('');
  const [genero, setGenero] = useState('');
  const [tipo, setTipo] = useState('livro'); // Novo estado para o tipo de obra
  const [msg, setMsg] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg('');

    try {
      // monta o payload incluindo genero e tipo só se houver algo digitado
      const payload = { titulo, ano, tipo };
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
      setGenero(''); 
      setTipo('livro'); // Limpa o campo tipo
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

      {/* Campo de seleção para o tipo de obra */}
      <div className="mb-4">
        <label className="block mb-2">Tipo de Obra</label>
        <div>
          <label>
            <input
              type="radio"
              name="tipo"
              value="livro"
              checked={tipo === 'livro'}
              onChange={() => setTipo('livro')}
            />
            Livro
          </label>
          <label className="ml-4">
            <input
              type="radio"
              name="tipo"
              value="artigo"
              checked={tipo === 'artigo'}
              onChange={() => setTipo('artigo')}
            />
            Artigo
          </label>
        </div>
      </div>

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
