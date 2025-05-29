import { Input } from "../ui/input";
import { Button } from "../ui/button";
import { Search } from "lucide-react";
import { useState } from "react";

export function AtualizarObra() {
  const [codBarrasBusca, setCodBarrasBusca] = useState("");
  const [obra, setObra] = useState<any | null>(null);
  const [erro, setErro] = useState("");
  const [mensagem, setMensagem] = useState("");

  const buscarObra = () => {
    fetch(`http://localhost:8080/api/obra/visualizar/${codBarrasBusca}`)
      .then((res) => {
        if (!res.ok) {
          throw new Error("Obra não encontrada");
        }
        return res.json();
      })
      .then((data) => {
        setObra(data);
        setErro("");
        setMensagem("");
      })
      .catch((err) => {
        setObra(null);
        setErro(err.message);
      });
  };

  const salvarAtualizacao = () => {
    if (!obra || !obra.cod_barras) return;

    fetch(`http://localhost:8080/api/obra/alterar?cod_barras=${obra.cod_barras}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(obra),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao atualizar");
        return res.text();
      })
      .then((mensagem) => {
        setMensagem(mensagem || "Obra atualizada com sucesso!");
        setErro("");
      })
      .catch((err) => {
        setErro("Erro ao salvar: " + err.message);
      });
  };
 // Test
  const handleChange = (campo: string, valor: string) => {
    setObra((prev: any) => ({ ...prev, [campo]: valor }));
  };

  return (
    <div>
      <div className="flex items-center m-5 mb-1">
        <Input
          placeholder="Digite o CÓDIGO DE BARRAS da obra"
          className="mr-2 placeholder:text-xl"
          value={codBarrasBusca}
          onChange={(e) => setCodBarrasBusca(e.target.value)}
        />
        <Button onClick={buscarObra} className="p-2 bg-gray-200 hover:bg-gray-300">
          <Search size={20} className="text-black" />
        </Button>
      </div>

      {erro && <p className="text-red-600 ml-5">{erro}</p>}
      {mensagem && <p className="text-green-600 ml-5">{mensagem}</p>}

      {obra && (
        <div className="gap-5">
          <div className="flex flex-col items-center m-5 mb-1 gap-4">
            <div className="flex flex-col items-start w-full gap-2">
              <p className="text-xl ml-2">Título</p>
              <Input
                placeholder="Digite o título"
                className="mr-2 placeholder:text-xl"
                value={obra.titulo || ""}
                onChange={(e) => handleChange("titulo", e.target.value)}
              />
            </div>
            <div className="flex flex-col items-start w-full gap-2">
              <p className="text-xl ml-2">Ano de Lançamento</p>
              <Input
                type="date"
                className="mr-2 placeholder:text-xl"
                value={obra.ano_lanc?.slice(0, 10) || ""}
                onChange={(e) => handleChange("ano_lanc", e.target.value)}
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

export default AtualizarObra;
