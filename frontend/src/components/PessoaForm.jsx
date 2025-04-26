import React, { useState } from 'react';

export default function PessoaForm() {
  const [id, setId] = useState('');        // Campo para o ID da pessoa (para atualização)
  const [nome, setNome] = useState('');
  const [numero, setNumero] = useState('');
  const [cep, setCep] = useState('');
  const [complemento, setComplemento] = useState('');
  const [matricula, setMatricula] = useState('');
  const [tipo, setTipo] = useState('cliente'); // Tipo de pessoa (cliente ou funcionario)
  const [historico, setHistorico] = useState(''); // Histórico só para cliente
  const [msg, setMsg] = useState('');
  const [isUpdating, setIsUpdating] = useState(false); // Controla se é atualização ou criação

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg('');
    try {
      // Verificando se os campos obrigatórios foram preenchidos
      if (!nome || !numero || !cep || !matricula) {
        setMsg('❌ Todos os campos obrigatórios devem ser preenchidos!');
        return;
      }

      // Montando o payload com os dados
      const payload = { nome, numero, cep, complemento, matricula, tipo };

      // Inclui o histórico no payload apenas se for cliente
      if (tipo === 'cliente' && historico.trim()) {
        payload.historico = historico.trim();
      }

      let res;
      if (isUpdating) {
        // Se for atualização, incluir o ID no payload
        payload.id = id;
        res = await fetch('http://localhost:8080/api/pessoa/atualizar', {
          method: 'PUT', // Usando PUT para atualização
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload),
        });
      } else {
        // Se for criação, enviar para o endpoint de criação
        res = await fetch('http://localhost:8080/api/pessoa/inserir', {
          method: 'POST', // Usando POST para criação
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload),
        });
      }

      const text = await res.text();
      if (!res.ok) throw new Error(text);

      setMsg(`✅ Pessoa ${isUpdating ? 'atualizada' : 'criada'} com sucesso!`);
      // Limpa os campos após o sucesso
      setId('');
      setNome('');
      setNumero('');
      setCep('');
      setComplemento('');
      setMatricula('');
      setHistorico('');
      setTipo('cliente'); // Reseta o tipo para cliente após o sucesso
      setIsUpdating(false); // Reseta o modo de atualização para criação
    } catch (e) {
      setMsg(`❌ ${e.message}`);
    }
  };

  return (
    <div>
      {/* Botões para escolher entre criar ou atualizar */}
      <div className="mb-4">
        <button
          type="button"
          className={`w-full py-2 mb-2 rounded ${isUpdating ? 'bg-gray-400' : 'bg-green-500'}`}
          onClick={() => setIsUpdating(false)} // Define o modo de criação
        >
          Criar Pessoa
        </button>
        <button
          type="button"
          className={`w-full py-2 mb-2 rounded ${!isUpdating ? 'bg-gray-400' : 'bg-blue-500'}`}
          onClick={() => setIsUpdating(true)} // Define o modo de atualização
        >
          Atualizar Pessoa
        </button>
      </div>

      <form onSubmit={handleSubmit} className="bg-white p-4 rounded-lg shadow">
        <h3 className="font-semibold mb-2">{isUpdating ? 'Atualizar' : 'Criar'} Pessoa</h3>

        {/* Campo para o ID da pessoa (apenas para atualização) */}
        {isUpdating && (
          <input
            className="w-full mb-2 p-2 border rounded"
            placeholder="ID da Pessoa"
            value={id}
            onChange={e => setId(e.target.value)}
          />
        )}

        {/* Campo para o nome */}
        <input
          className="w-full mb-2 p-2 border rounded"
          placeholder="Nome"
          value={nome}
          onChange={e => setNome(e.target.value)}
        />

        {/* Campo para o número */}
        <input
          className="w-full mb-2 p-2 border rounded"
          placeholder="Número"
          value={numero}
          onChange={e => setNumero(e.target.value)}
        />

        {/* Campo para o CEP */}
        <input
          className="w-full mb-2 p-2 border rounded"
          placeholder="CEP"
          value={cep}
          onChange={e => setCep(e.target.value)}
        />

        {/* Campo para o complemento */}
        <input
          className="w-full mb-2 p-2 border rounded"
          placeholder="Complemento"
          value={complemento}
          onChange={e => setComplemento(e.target.value)}
        />

        {/* Campo para a matrícula */}
        <input
          className="w-full mb-2 p-2 border rounded"
          placeholder="Matrícula"
          value={matricula}
          onChange={e => setMatricula(e.target.value)}
        />

        {/* Seleção de tipo de pessoa (Cliente ou Funcionário) */}
        <div className="mb-4">
          <label className="block mb-2">Tipo de Pessoa</label>
          <div>
            <label>
              <input
                type="radio"
                name="tipo"
                value="cliente"
                checked={tipo === 'cliente'}
                onChange={() => setTipo('cliente')}
              />
              Cliente
            </label>
            <label className="ml-4">
              <input
                type="radio"
                name="tipo"
                value="funcionario"
                checked={tipo === 'funcionario'}
                onChange={() => setTipo('funcionario')}
              />
              Funcionário
            </label>
          </div>
        </div>

        {/* Exibe o campo histórico apenas se o tipo for cliente */}
        {tipo === 'cliente' && (
          <div className="mb-2">
            <input
              className="w-full p-2 border rounded"
              placeholder="Histórico"
              value={historico}
              onChange={e => setHistorico(e.target.value)}
            />
          </div>
        )}

        {/* Botão para enviar o formulário */}
        <button
          type="submit"
          className="w-full py-2 bg-green-500 text-white rounded hover:bg-green-600"
        >
          {isUpdating ? 'Atualizar Pessoa' : 'Criar Pessoa'}
        </button>

        {/* Exibe mensagem de sucesso ou erro */}
        {msg && <p className="mt-2 text-sm">{msg}</p>}
      </form>
    </div>
  );
}
