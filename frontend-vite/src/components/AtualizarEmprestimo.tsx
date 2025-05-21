import { useState } from "react";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import { Search } from "lucide-react";

function AtualizarEmprestimo() {
  const [idBusca, setIdBusca] = useState("");
  const [emprestimo, setEmprestimo] = useState<any | null>(null);
  const [erro, setErro] = useState("");
  const [mensagem, setMensagem] = useState("");

  const buscarEmprestimo = () => {
    fetch(`http://localhost:8080/api/emprestimo/visualizar/${idBusca}`)
      .then((res) => res.json())
      .then((data) => {
        if (!data || Object.keys(data).length === 0) {
          setEmprestimo(null);
          setErro("Empréstimo não encontrado");
          return;
        }
        setEmprestimo(data);
        setErro("");
        setMensagem("");
      })
      .catch((err) => {
        setEmprestimo(null);
        setErro("Erro ao buscar empréstimo: " + err.message);
      });
  };


  const salvarAtualizacao = () => {
    if (!emprestimo || !emprestimo.id) return;

    fetch(`http://localhost:8080/api/emprestimo/alterar?id=${emprestimo.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(emprestimo),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao atualizar");
        return res.text(); // o backend retorna uma String, não JSON
      })
      .then((mensagem) => {
        setMensagem(mensagem || "Empréstimo atualizado com sucesso!");
        setErro("");
      })
      .catch((err) => {
        setErro("Erro ao salvar: " + err.message);
      });
  };


  const handleChange = (campo: string, valor: string) => {
    setEmprestimo((prev: any) => ({ ...prev, [campo]: valor }));
  };

  return (
    <div>
      <div className="flex items-center m-5 mb-1">
        <Input
          placeholder="Digite o ID do empréstimo"
          className="mr-2 placeholder:text-xl"
          value={idBusca}
          onChange={(e) => setIdBusca(e.target.value)}
        />
        <Button onClick={buscarEmprestimo} className="p-2 bg-gray-200 hover:bg-gray-300">
          <Search size={20} className="text-black" />
        </Button>
      </div>

      {erro && <p className="text-red-600 ml-5">{erro}</p>}
      {mensagem && <p className="text-green-600 ml-5">{mensagem}</p>}

      {emprestimo && (
        <div className="gap-5">
          <div className="flex flex-col items-center m-5 mb-1 gap-4">
            <div className="flex flex-col items-start w-full gap-2">
              <p className="text-xl ml-2">Exemplar</p>
              <Input
                placeholder="Digite id do exemplar"
                className="mr-2 placeholder:text-xl"
                value={emprestimo.fkExemplar || ""}
                onChange={(e) => handleChange("fkExemplar", e.target.value)}
              />
            </div>
            <div className="flex flex-col items-start w-full gap-2">
              <p className="text-xl ml-2">Cliente</p>
              <Input
                placeholder="Digite id do cliente"
                className="mr-2 placeholder:text-xl"
                value={emprestimo.fkCliente || ""}
                onChange={(e) => handleChange("fkCliente", e.target.value)}
              />
            </div>
            <div className="flex flex-col items-start w-full gap-2">
              <p className="text-xl ml-2">Data do empréstimo</p>
              <Input
                type="datetime-local"
                className="mr-2 placeholder:text-xl"
                value={emprestimo.dataEmprestimo?.slice(0, 16) || ""}
                onChange={(e) => handleChange("dataEmprestimo", e.target.value)}
              />
            </div>
            <div className="flex flex-col items-start w-full gap-2">
              <p className="text-xl ml-2">Data prevista da devolução</p>
              <Input
                type="datetime-local"
                className="mr-2 placeholder:text-xl"
                value={emprestimo.dataPrevistaDev?.slice(0, 16) || ""}
                onChange={(e) => handleChange("dataPrevistaDev", e.target.value)}
              />
            </div>
            <div className="flex flex-col items-start w-full gap-2">
              <p className="text-xl ml-2">Data da devolução</p>
              <Input
                type="date"
                className="mr-2 placeholder:text-xl"
                value={emprestimo.dataDevolucao?.slice(0, 16) || ""}
                onChange={(e) => handleChange("dataDevolucao", e.target.value)}
              />
            </div>
          </div>

          <div className="flex justify-end m-4 mb-2">
            <Button
              onClick={salvarAtualizacao}
              className="p-4 bg-gray-200 hover:bg-gray-300 text-black"
            >
              Salvar
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}

export default AtualizarEmprestimo;
