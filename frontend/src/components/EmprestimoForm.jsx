import React, { useState } from 'react';

const formatTime = t => t && t.split(':').length === 2 ? `${t}:00` : t;

export default function EmprestimoForm({ onSuccess }) {
  const [hora, setHora] = useState('');
  const [prevista, setPrevista] = useState('');
  const [devolucao, setDevolucao] = useState('');
  const [emprestimo, setEmprestimo] = useState('');
  const [exemplar, setExemplar] = useState('');
  const [cliente, setCliente] = useState('');
  const [msg, setMsg] = useState('');

  const handleSubmit = async e => {
    e.preventDefault();
    setMsg('');
    const payload = {
      id: crypto.randomUUID(),
      hora: formatTime(hora),
      dataPrevistaDev: formatTime(prevista),
      dataDevolucao: formatTime(devolucao),
      dataEmprestimo: formatTime(emprestimo),
      fkExemplar: exemplar,
      fkCliente: cliente
    };

    try {
      const res = await fetch('http://localhost:8080/api/emprestimo/inserir', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      const text = await res.text();
      if (!res.ok) throw new Error(text);
      setMsg(`✅ ${text}`);
      setHora(''); setPrevista(''); setDevolucao(''); setEmprestimo('');
      setExemplar(''); setCliente('');
      onSuccess?.();
    } catch (err) {
      setMsg(`❌ ${err.message}`);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="bg-white p-4 rounded-lg shadow">
      <h3 className="font-semibold mb-2">Novo Empréstimo</h3>
      <label className="block mb-2">
        Hora:
        <input type="time" value={hora} onChange={e => setHora(e.target.value)}
               className="w-full p-2 border rounded" />
      </label>
      <label className="block mb-2">
        Prevista Devolução:
        <input type="datetime-local" value={prevista} onChange={e => setPrevista(e.target.value)}
               className="w-full p-2 border rounded" />
      </label>
      <label className="block mb-2">
        Data Devolução:
        <input type="date" value={devolucao} onChange={e => setDevolucao(e.target.value)}
               className="w-full p-2 border rounded" />
      </label>
      <label className="block mb-2">
        Data Empréstimo:
        <input type="date" value={emprestimo} onChange={e => setEmprestimo(e.target.value)}
               className="w-full p-2 border rounded" />
      </label>
      <label className="block mb-2">
        Exemplar ID:
        <input type="text" value={exemplar} onChange={e => setExemplar(e.target.value)}
               className="w-full p-2 border rounded" />
      </label>
      <label className="block mb-2">
        Cliente ID:
        <input type="text" value={cliente} onChange={e => setCliente(e.target.value)}
               className="w-full p-2 border rounded" />
      </label>
      <button type="submit"
              className="w-full py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
        Inserir
      </button>
      {msg && <p className="mt-2 text-sm">{msg}</p>}
    </form>
  );
}
