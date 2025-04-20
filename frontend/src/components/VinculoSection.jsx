import React, { useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

export default function VinculoSection() {
  const [obra, setObra] = useState('');
  const [editora, setEditora] = useState('');
  const [autor, setAutor] = useState('');
  const [msg, setMsg] = useState('');

  async function vincular(path, body, label) {
    setMsg('');
    try {
      const res = await fetch(`http://localhost:8080/api/${path}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      });
      const text = await res.text();
      if (!res.ok) throw new Error(text);
      setMsg(`✅ ${label}: ${text}`);
    } catch (e) {
      setMsg(`❌ Erro ${label}: ${e.message}`);
    }
  }

  return (
    <div className="bg-white p-4 rounded-lg shadow space-y-4">
      <h3 className="font-semibold">Vinculações</h3>

      {/* Vincular Obra-Editora */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
        <input
          className="p-2 border rounded"
          placeholder="Código de Barras (Obra)"
          value={obra}
          onChange={e => setObra(e.target.value)}
        />
        <input
          className="p-2 border rounded"
          placeholder="ID da Editora"
          value={editora}
          onChange={e => setEditora(e.target.value)}
        />
      </div>
      <button
        onClick={() => vincular('editora/vincular', { codBarras: obra, idEditora: editora }, 'Obra↔Editora')}
        className="px-4 py-2 bg-purple-500 text-white rounded hover:bg-purple-600"
      >Vincular Obra-Editora</button>

      {/* Vincular Autor-Obra */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
        <input
          className="p-2 border rounded"
          placeholder="ID Autor"
          value={autor}
          onChange={e => setAutor(e.target.value)}
        />
        <input
          className="p-2 border rounded"
          placeholder="Código de Barras (Obra)"
          value={obra}
          onChange={e => setObra(e.target.value)}
        />
      </div>
      <button
        onClick={() => vincular('autor/vincular', { idAutor: autor, codBarras: obra }, 'Autor↔Obra')}
        className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
      >Vincular Autor-Obra</button>

      {msg && <p className="mt-2 text-sm">{msg}</p>}
    </div>
  );
}
