import { Input } from "../ui/input";
import { Button } from "../ui/button";
import { Search } from "lucide-react";
import { useState } from "react";

export function AtualizarCliente() {
  const [clienteIdBusca, setClienteIdBusca] = useState("");
  const [cliente, setCliente] = useState<any | null>(null);
  const [erro, setErro] = useState("");
  const [mensagem, setMensagem] = useState("");

  const buscarCliente = () => {
    fetch(`http://localhost:8080/api/cliente/visualizar`)
      .then((res) => {
        if (!res.ok) {
          throw new Error("Erro ao buscar clientes");
        }
        return res.json();
      })
      .then((data) => {
        const clienteEncontrado = data.find((c: any) => c.id === clienteIdBusca);
        if (!clienteEncontrado) {
          throw new Error("Cliente não encontrado");
        }
        setCliente(clienteEncontrado);
        setErro("");
        setMensagem("");
      })
      .catch((err) => {
        setCliente(null);
        setErro(err.message);
      });
  };

  const salvarAtualizacao = () => {
    if (!cliente || !cliente.id) return;

    fetch(`http://localhost:8080/api/cliente/atualizar`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        id: cliente.id,
        nomePessoa: cliente.nomePessoa,
        historico: cliente.historico
      }),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao atualizar");
        return res.text();
      })
      .then((mensagem) => {
        setMensagem(mensagem || "Cliente atualizado com sucesso!");
        setErro("");
      })
      .catch((err) => {
        setErro("Erro ao salvar: " + err.message);
      });
  };

  const handleChange = (campo: string, valor: string) => {
    setCliente((prev: any) => ({ ...prev, [campo]: valor }));
  };

  return (
    <div>
      <div className="flex items-center m-5 mb-1">
        <Input
          placeholder="Digite o ID do cliente"
          className="mr-2 placeholder:text-xl"
          value={clienteIdBusca}
          onChange={(e) => setClienteIdBusca(e.target.value)}
        />
        <Button onClick={buscarCliente} className="p-2 bg-gray-200 hover:bg-gray-300">
          <Search size={20} className="text-black" />
        </Button>
      </div>

      {erro && <p className="text-red-600 ml-5">{erro}</p>}
      {mensagem && <p className="text-green-600 ml-5">{mensagem}</p>}

      {cliente && (
        <div className="gap-5">
          <div className="flex flex-col items-center m-5 mb-1 gap-4">
            <div className="flex flex-col items-start w-full gap-2">
              <p className="text-xl ml-2">Nome da Pessoa</p>
              <Input
                placeholder="Digite o nome"
                className="mr-2 placeholder:text-xl"
                value={cliente.nomePessoa || ""}
                onChange={(e) => handleChange("nomePessoa", e.target.value)}
              />
            </div>
            <div className="flex flex-col items-start w-full gap-2">
              <p className="text-xl ml-2">Histórico</p>
              <Input
                placeholder="Digite o histórico"
                className="mr-2 placeholder:text-xl"
                value={cliente.historico || ""}
                onChange={(e) => handleChange("historico", e.target.value)}
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

export default AtualizarCliente;