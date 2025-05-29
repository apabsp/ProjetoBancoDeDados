import { useState } from "react";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import { Search } from "lucide-react";

function DeletarExemplar() {
  const [idParaDeletar, setIdParaDeletar] = useState("");

  const deletarExemplar = () => {
    if (!idParaDeletar) {
      alert("Digite um ID válido.");
      return;
    }

    fetch(`http://localhost:8080/api/exemplar/deletar?id=${idParaDeletar}`, {
      method: "DELETE",
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Erro ao deletar exemplar.");
        }
        alert(`Exemplar ${idParaDeletar} deletado com sucesso.`);
        setIdParaDeletar("");
      })
      .catch((error) => {
        console.error("Erro ao deletar exemplar:", error);
        alert("Erro ao deletar exemplar.");
      });
  };

  return (
    <div className="gap-5">
      <div className="flex items-center m-5 mb-1">
        <Input
          placeholder="Digite o ID do exemplar"
          className="mr-2 placeholder:text-xl"
          value={idParaDeletar}
          onChange={(e) => setIdParaDeletar(e.target.value)}
        />
        <Button className="p-2 bg-gray-200 hover:bg-gray-300">
          <Search size={20} className="text-black" />
        </Button>
      </div>
      <div className="flex justify-end m-4 mb-2">
        <Button
          className="p-4 bg-gray-200 hover:bg-gray-300 text-black"
          onClick={deletarExemplar}
        >
          Deletar
        </Button>
      </div>
    </div>
  );
}

export default DeletarExemplar;
