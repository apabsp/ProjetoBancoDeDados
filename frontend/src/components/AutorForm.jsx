import React, { useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

export default function AutorForm() {
  const [nome, setNome] = useState('');
  const [msg, setMsg] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg('');
    try {
      const id = uuidv4();
      const res = await fetch('http://localhost:8080/api/autor/inserir', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id, nome }),
      });
      const text = await res.text();
      if (!res.ok) throw new Error(text);
      setMsg(`✅ ${text}`);
      setNome('');
    } catch (e) {
      setMsg(`❌ ${e.message}`);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="bg-white p-4 rounded-lg shadow">
      <h3 className="font-semibold mb-2">Novo Autor</h3>
      <input
        className="w-full mb-2 p-2 border rounded"
        placeholder="Nome do Autor"
        value={nome}
        onChange={e => setNome(e.target.value)}
      />
      <button
        type="submit"
        className="w-full py-2 bg-yellow-500 text-white rounded hover:bg-yellow-600"
      >Inserir Autor</button>
      {msg && <p className="mt-2 text-sm">{msg}</p>}
    </form>
  );
}
