import { useState } from "react";
import { Input } from "./ui/input";
import { Button } from "./ui/button";

function InserirEmprestimo() {
  const [fkExemplar, setFkExemplar] = useState("");
  const [fkCliente, setFkCliente] = useState("");
  const [fkFuncionario, setFkFuncionario] = useState("");
  const [dataEmprestimo, setDataEmprestimo] = useState("");
  const [dataPrevistaDev, setDataPrevistaDev] = useState("");
  const [mensagem, setMensagem] = useState(""); // Pra mostrar mensagem data dps

  const handleSubmit = () => {
    const payload = {
      fkExemplar,
      fkCliente,
      fkFuncionario,
      dataEmprestimo: dataEmprestimo || null,
      dataPrevistaDev: dataPrevistaDev || null
    };

    fetch("http://localhost:8080/api/emprestimo/inserir", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    })
      .then(res => res.text())
      .then(data => {
        setMensagem(data);
        console.log("Resposta do backend:", data);
      })
      .catch(error => {
        console.error("Erro ao inserir empréstimo:", error);
        setMensagem("Erro ao inserir empréstimo.");
      });
  };

  return (
    <div className="flex flex-col items-start m-5 gap-4">
      <div className="w-full">
        <p className="text-xl mb-1">ID do Exemplar</p>
        <Input value={fkExemplar} onChange={e => setFkExemplar(e.target.value)} placeholder="Digite o ID do exemplar" />
      </div>
      <div className="w-full">
        <p className="text-xl mb-1">ID do Cliente</p>
        <Input value={fkCliente} onChange={e => setFkCliente(e.target.value)} placeholder="Digite o ID do cliente" />
      </div>
      <div className="w-full">
        <p className="text-xl mb-1">ID do Funcionário</p>
        <Input value={fkFuncionario} onChange={e => setFkFuncionario(e.target.value)} placeholder="Digite o ID do funcionário" />
      </div>
      <div className="w-full">
        <p className="text-xl mb-1">Data do Empréstimo</p>
        <Input type="datetime-local" value={dataEmprestimo} onChange={e => setDataEmprestimo(e.target.value)} />
      </div>
      <div className="w-full">
        <p className="text-xl mb-1">Data Prevista da Devolução</p>
        <Input type="datetime-local" value={dataPrevistaDev} onChange={e => setDataPrevistaDev(e.target.value)} />
      </div>
      <Button className="mt-4 p-4 bg-gray-200 hover:bg-gray-300 text-black" onClick={handleSubmit}>
        Salvar
      </Button>
      {mensagem && <p className="text-md mt-2 text-green-600">{mensagem}</p>}
    </div>
  );
}

export default InserirEmprestimo;
