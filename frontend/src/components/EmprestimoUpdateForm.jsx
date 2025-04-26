import React, { useState } from 'react';

function EmprestimoUpdateForm() {
    const [idEmprestimo, setIdEmprestimo] = useState('');
    const [hora, setHora] = useState('');
    const [dataDevolucao, setDataDevolucao] = useState('');
    const [fkFuncionario, setFkFuncionario] = useState(''); 
    const [message, setMessage] = useState('');
    console.log({ idEmprestimo, hora, dataDevolucao, fkFuncionario }); // Log the form state

    const handleUpdate = async () => {
        console.log({ idEmprestimo, hora, dataDevolucao, fkFuncionario }); // Log the form state
        try {
            const res = await fetch('http://localhost:8080/api/emprestimo/alterar', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    idEmprestimo, hora, dataDevolucao, fkFuncionario
                })
            });

            const text = await res.text();
            if (!res.ok) throw new Error(text);
            setMessage(`✅ ${text}`);
            setIdEmprestimo('');
            setHora('');
            setDataDevolucao('');
            setFkFuncionario('');
        } catch (e) {
            setMessage(`❌ ${e.message}`);
        }
    };

    return (
        <form onSubmit={e => e.preventDefault()} className="bg-white p-4 rounded-lg shadow">
            <h3 className="font-semibold mb-2">Alterar Emprestimo</h3>
            <input
                type="text"
                className="w-full mb-2 p-2 border rounded"
                placeholder="ID do Empréstimo"
                value={idEmprestimo}
                onChange={e => setIdEmprestimo(e.target.value)}
            />
            <input
                type="time"
                className="w-full mb-2 p-2 border rounded"
                placeholder="Hora"
                value={hora}
                onChange={e => setHora(e.target.value)}
            />
            <input
                type="date"
                className="w-full mb-2 p-2 border rounded"
                value={dataDevolucao}
                onChange={e => setDataDevolucao(e.target.value)}
            />
            <input
                type="text"
                className="w-full mb-2 p-2 border rounded"
                placeholder="ID do Funcionário"
                value={fkFuncionario}
                onChange={e => setFkFuncionario(e.target.value)}
            />
            <button
                type="submit"
                onClick={handleUpdate}
                className="w-full py-2 bg-green-500 text-white rounded hover:bg-green-600"
            >
                Atualizar Emprestimo
            </button>
            {message && <p className="mt-2 text-sm">{message}</p>}
        </form>
    );
}

export default EmprestimoUpdateForm;
