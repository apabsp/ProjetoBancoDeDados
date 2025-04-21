import React, { useState } from 'react';

export default function DeleteEmprestimoForm({ onDelete }) {
  const [id, setId] = useState('');
  const [msg, setMsg] = useState('');

  const handleDelete = async e => {
    e.preventDefault();
    setMsg('');
    try {
      const res = await fetch(
        `http://localhost:8080/api/emprestimo/delete?id=${encodeURIComponent(id)}`,
        { method: 'DELETE' }
      );
      const text = await res.text();
      if (!res.ok) throw new Error(text);
      setMsg(`✅ ${text}`);
      setId('');
      onDelete?.();
    } catch (err) {
      setMsg(`❌ ${err.message}`);
    }
  };

  return (
    <form onSubmit={handleDelete} className="bg-white p-4 rounded-lg shadow">
      <h3 className="font-semibold mb-2">Deletar Empréstimo</h3>
      <input
        type="text"
        placeholder="ID do Empréstimo"
        value={id}
        onChange={e => setId(e.target.value)}
        className="w-full mb-2 p-2 border rounded"
      />
      <button type="submit"
              className="w-full py-2 bg-red-500 text-white rounded hover:bg-red-600">
        Deletar
      </button>
      {msg && <p className="mt-2 text-sm">{msg}</p>}
    </form>
  );
}
