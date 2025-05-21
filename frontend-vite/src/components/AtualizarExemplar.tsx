import { useState } from "react";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import { Search } from "lucide-react";

function AtualizarExemplar() {
  const [idBusca, setIdBusca] = useState("");
  const [exemplar, setExemplar] = useState<any | null>(null);
  const [erro, setErro] = useState("");
  const [mensagem, setMensagem] = useState("");

  const buscarExemplar = () => {
    fetch(`http://localhost:8080/api/exemplar/visualizar/${idBusca}`)
      .then((res) => res.json())
      .then((data) => {
        if (!data || Object.keys(data).length === 0) {
          setExemplar(null);
          setErro("Exemplar não encontrado");
          return;
        }
        setExemplar(data);
        setErro("");
        setMensagem("");
      })
      .catch((err) => {
        setExemplar(null);
        setErro("Erro ao buscar exemplar: " + err.message);
      });
  };

  const salvarAtualizacao = () => {
    if (!exemplar || !exemplar.id) return;

    fetch(`http://localhost:8080/api/exemplar/alterar?id=${exemplar.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(exemplar),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao atualizar");
        return res.text();
      })
      .then((mensagem) => {
        setMensagem(mensagem || "Exemplar atualizado com sucesso!");
        setErro("");
      })
      .catch((err) => {
        setErro("Erro ao salvar: " + err.message);
      });
  };

  const handleChange = (campo: string, valor: string) => {
    setExemplar((prev: any) => ({ ...prev, [campo]: valor }));
  };

  return (
    <div>
      <div className="flex items-center m-5 mb-1">
        <Input
          placeholder="Digite o ID do exemplar"
          className="mr-2 placeholder:text-xl"
          value={idBusca}
          onChange={(e) => setIdBusca(e.target.value)}
        />
        <Button onClick={buscarExemplar} className="p-2 bg-gray-200 hover:bg-gray-300">
          <Search size={20} className="text-black" />
        </Button>
      </div>

      {erro && <p className="text-red-600 ml-5">{erro}</p>}
      {mensagem && <p className="text-green-600 ml-5">{mensagem}</p>}

      {exemplar && (
        <div className="gap-5">
          <div className="flex flex-col items-center m-5 mb-1 gap-4">
            <div className="flex flex-col items-start w-full gap-2">
              <p className="text-xl ml-2">ID da Edição</p>
              <Input
                placeholder="Digite id da edição"
                className="mr-2 placeholder:text-xl"
                value={exemplar.fkEdicao || ""}
                onChange={(e) => handleChange("fkEdicao", e.target.value)}
              />
            </div>
            <div className="flex flex-col items-start w-full gap-2">
              <p className="text-xl ml-2">ID do Artigo</p>
              <Input
                placeholder="Digite id do artigo"
                className="mr-2 placeholder:text-xl"
                value={exemplar.fkArtigo || ""}
                onChange={(e) => handleChange("fkArtigo", e.target.value)}
              />
            </div>
            <div className="flex flex-col items-start w-full gap-2">
              <p className="text-xl ml-2">Número da Estante</p>
              <Input
                placeholder="Digite número da estante"
                className="mr-2 placeholder:text-xl"
                value={exemplar.fkEstanteNumero || ""}
                onChange={(e) => handleChange("fkEstanteNumero", e.target.value)}
              />
            </div>
            <div className="flex flex-col items-start w-full gap-2">
              <p className="text-xl ml-2">Prateleira da Estante</p>
              <Input
                placeholder="Digite prateleira da estante"
                className="mr-2 placeholder:text-xl"
                value={exemplar.fkEstantePrateleira || ""}
                onChange={(e) => handleChange("fkEstantePrateleira", e.target.value)}
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

export default AtualizarExemplar;
