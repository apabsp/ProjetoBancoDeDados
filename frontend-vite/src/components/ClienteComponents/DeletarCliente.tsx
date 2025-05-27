import { Input } from "../ui/input";
import { Search } from "lucide-react";
import { useState } from "react";
import { Button } from "../ui/button";

export function DeletarCliente() {
  const [clienteIdParaDeletar, setClienteIdParaDeletar] = useState("");

  const deletarCliente = () => {
    if (!clienteIdParaDeletar) {
      alert("Digite um ID de cliente válido.");
      return;
    }

    fetch(`http://localhost:8080/api/cliente/deletar/${clienteIdParaDeletar}`, {
      method: "DELETE",
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Erro ao deletar cliente.");
        }
        return res.text();
      })
      .then((message) => {
        alert(message || `Cliente com ID ${clienteIdParaDeletar} deletado com sucesso.`);
        setClienteIdParaDeletar("");
      })
      .catch((error) => {
        console.error("Erro ao deletar:", error);
        alert(error.message || "Erro ao deletar cliente.");
      });
  };

  return (
    <div className="gap-5">
      <div className="flex items-center m-5 mb-1">
        <Input
          placeholder="Digite o ID do cliente"
          className="mr-2 placeholder:text-xl"
          value={clienteIdParaDeletar}
          onChange={(e) => setClienteIdParaDeletar(e.target.value)}
        />
        <Button 
          className="p-2 bg-gray-200 hover:bg-gray-300" 
          onClick={deletarCliente}
        >
          <Search size={20} className="text-black" />
        </Button>
      </div>
      <div className="flex justify-end m-4 mb-2">
        <Button
          className="p-4 bg-red-200 hover:bg-red-300 text-black"
          onClick={deletarCliente}
        >
          Confirmar Deleção
        </Button>
      </div>
    </div>
  );
}

export default DeletarCliente;