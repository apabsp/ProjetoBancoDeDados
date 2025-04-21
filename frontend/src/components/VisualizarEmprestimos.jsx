import React, { useState, useEffect } from 'react';

export default function VisualizarEmprestimos({ refreshKey }) {
  const [lista, setLista] = useState([]);

  const fetchData = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/emprestimo/visualizar');
      const data = await res.json();
      setLista(data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchData();
  }, [refreshKey]);

  return (
    <div className="bg-white p-4 rounded-lg shadow">
      <h3 className="font-semibold mb-2">Empréstimos Cadastrados</h3>
      <button onClick={fetchData}
              className="mb-2 px-2 py-1 bg-gray-200 rounded">
        Atualizar
      </button>
      <ul className="list-disc list-inside">
        {lista.map(item => <li key={item}>{item}</li>)}
      </ul>
    </div>
  );
}
