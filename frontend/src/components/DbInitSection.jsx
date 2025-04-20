import React, { useState } from 'react';

export default function DbInitSection() {
  const [msg, setMsg] = useState('');
  const [loading, setLoading] = useState(false);

  async function exec(path, label) {
    setLoading(true);
    setMsg('');
    try {
      const res = await fetch(`http://localhost:8080/api/init${path}`, { method: 'POST' });
      if (!res.ok) throw new Error(res.statusText);
      const text = await res.text();
      setMsg(`${label}: ${text}`);
    } catch (e) {
      setMsg(`Erro em ${label}: ${e.message}`);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="bg-white p-4 rounded-lg shadow">
      <h2 className="text-xl font-semibold mb-2">Inicialização do Banco</h2>
      <div className="flex gap-2">
        <button
          disabled={loading}
          onClick={() => exec('', 'Database completa')}
          className="px-4 py-2 bg-indigo-500 text-white rounded hover:bg-indigo-600 disabled:opacity-50"
        >
          Criar tudo
        </button>
        <button
          disabled={loading}
          onClick={() => exec('/obra', 'Tabela Obra')}
          className="px-4 py-2 bg-indigo-500 text-white rounded hover:bg-indigo-600 disabled:opacity-50"
        >
          Tabela Obra
        </button>
      </div>
      {msg && <p className="mt-2 text-gray-700">{msg}</p>}
    </div>
  );
}