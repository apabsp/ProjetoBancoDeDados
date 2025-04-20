import React, { useState } from 'react';

export default function VisualizarObras() {
  const [idEditora, setIdEditora] = useState('');
  const [resultado, setResultado] = useState('');

  const handleVisualizar = async () => {
    setResultado('Carregando...');
    try {
      const res = await fetch('http://localhost:8080/api/editora/visualizar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ idEditora })
      });
      const text = await res.text();
      if (!res.ok) throw new Error(text);
      setResultado(text);
    } catch (e) {
      setResultado(`Erro: ${e.message}`);
    }
  };

  return (
    <div className="bg-white p-4 rounded-lg shadow">
      <h3 className="font-semibold mb-2">Visualizar Obras por Editora</h3>
      <div className="flex gap-2 mb-2">
        <input
          className="flex-1 p-2 border rounded"
          placeholder="ID da Editora"
          value={idEditora}
          onChange={e => setIdEditora(e.target.value)}
        />
        <button
          onClick={handleVisualizar}
          className="px-4 py-2 bg-teal-500 text-white rounded hover:bg-teal-600"
        >Buscar</button>
      </div>
      {resultado && <pre className="whitespace-pre-wrap text-sm text-gray-700">{resultado}</pre>}
    </div>
  );
}