import React, { useState, useEffect } from 'react';

export default function ObraList() {
const [obras, setObras] = useState([]); // Estado para armazenar as obras
const [msg, setMsg] = useState(''); // Mensagens de sucesso ou erro

// Função para buscar as obras do backend
const fetchObras = async () => {
    setMsg(''); // Limpar mensagem anterior
    try {
    const res = await fetch('http://localhost:8080/api/obra/listar'); // Endpoint para listar as obras
    const data = await res.json();

    if (Array.isArray(data)) {
        setObras(data); // Armazenar as obras no estado
    } else {
        throw new Error('Erro ao carregar as obras');
    }
    } catch (error) {
    setMsg(`❌ ${error.message}`);
    }
};

// Usar useEffect para chamar a função quando o componente for montado
useEffect(() => {
    fetchObras();
}, []);

return (
    <div className="bg-white p-4 rounded-lg shadow">
    <h3 className="font-semibold mb-2">Lista de Obras</h3>

    {/* Exibe mensagem de erro ou sucesso */}
    {msg && <p className="text-sm text-red-500">{msg}</p>}

    {/* Tabela para exibir as obras */}
    <table className="min-w-full table-auto border-collapse border border-gray-300">
        <thead>
        <tr>
            <th className="border px-4 py-2">Título</th>
            <th className="border px-4 py-2">Ano</th>
            <th className="border px-4 py-2">Gênero</th>
            <th className="border px-4 py-2">Tipo</th>
            <th className="border px-4 py-2">Editora</th>
        </tr>
        </thead>
        <tbody>
        {obras.length > 0 ? (
            obras.map((obra) => (
            <tr key={obra.id}>
                <td className="border px-4 py-2">{obra.titulo}</td>
                <td className="border px-4 py-2">{obra.ano}</td>
                <td className="border px-4 py-2">{obra.genero || 'N/A'}</td>
                <td className="border px-4 py-2">{obra.tipo}</td>
                <td className="border px-4 py-2">{obra.editora?.nome || 'N/A'}</td>
            </tr>
            ))
        ) : (
            <tr>
            <td colSpan="5" className="border px-4 py-2 text-center">Nenhuma obra encontrada</td>
            </tr>
        )}
        </tbody>
    </table>

    {/* Botão para recarregar a lista de obras */}
    <button
        onClick={fetchObras}
        className="mt-4 py-2 px-4 bg-blue-500 text-white rounded hover:bg-blue-600"
    >
        Recarregar Obras
    </button>
    </div>
);
}
