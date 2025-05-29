import { Input } from "../ui/input";
import { Search } from "lucide-react";
import { useState } from "react";
import { Button } from "../ui/button";

export function DeletarObra() {
  const [codBarrasParaDeletar, setCodBarrasParaDeletar] = useState("");

  const deletarObra = () => {
    if (!codBarrasParaDeletar) {
      alert("Digite um código de barras válido.");
      return;
    }

    fetch(`http://localhost:8080/api/obra/deletar/${codBarrasParaDeletar}`, {
      method: "GET",
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Erro ao deletar obra.");
        }
        alert(`Obra com código de barras ${codBarrasParaDeletar} deletada com sucesso.`);
        setCodBarrasParaDeletar("");
      })
      .catch((error) => {
        console.error("Erro ao deletar:", error);
        alert("Erro ao deletar obra.");
      });
  };

  return (
    <div className="gap-5">
      <div className="flex items-center m-5 mb-1">
        <Input
          placeholder="Digite o CÓDIGO DE BARRAS da obra"
          className="mr-2 placeholder:text-xl"
          value={codBarrasParaDeletar}
          onChange={(e) => setCodBarrasParaDeletar(e.target.value)}
        />
        <Button className="p-2 bg-gray-200 hover:bg-gray-300" onClick={deletarObra}>
          <Search size={20} className="text-black" />
        </Button>
      </div>
      <div className="flex justify-end m-4 mb-2">
        <Button
          
          onClick={deletarObra}
        >
          Deletar
        </Button>
      </div>
    </div>
  );
}

export default DeletarObra;
