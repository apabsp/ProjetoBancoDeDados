import { useState } from "react";
import { Input } from "../ui/input";
import { Button } from "../ui/button";

function InserirObra() {
  const [codBarras, setCodBarras] = useState("");
  const [titulo, setTitulo] = useState("");
  const [anoLanc, setAnoLanc] = useState("");
  const [mensagem, setMensagem] = useState("");

  const handleSubmit = () => {
    const payload = {
      cod_barras: codBarras,
      titulo,
      ano_lanc: anoLanc || null,
    };

    fetch("http://localhost:8080/api/obra/inserir", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    })
      .then((res) => res.text())
      .then((data) => {
        setMensagem(data);
        console.log("Resposta do backend:", data);
        setCodBarras("");
        setTitulo("");
        setAnoLanc("");
      })
      .catch((error) => {
        console.error("Erro ao inserir obra:", error);
        setMensagem("Erro ao inserir obra.");
      });
  };

  return (
    <div className="flex flex-col items-start m-5 gap-4">
      <div className="w-full">
        <p className="text-xl mb-1">Código de Barras</p>
        <Input
          value={codBarras}
          onChange={(e) => setCodBarras(e.target.value)}
          placeholder="Digite o código de barras"
        />
      </div>
      <div className="w-full">
        <p className="text-xl mb-1">Título</p>
        <Input
          value={titulo}
          onChange={(e) => setTitulo(e.target.value)}
          placeholder="Digite o título da obra"
        />
      </div>
      <div className="w-full">
        <p className="text-xl mb-1">Ano de Lançamento</p>
        <Input
          type="date"
          value={anoLanc}
          onChange={(e) => setAnoLanc(e.target.value)}
        />
      </div>
      <Button
        className="mt-4 p-4 bg-gray-200 hover:bg-gray-300 text-black"
        onClick={handleSubmit}
      >
        Salvar
      </Button>
      {mensagem && <p className="text-md mt-2 text-green-600">{mensagem}</p>}
    </div>
  );
}

export default InserirObra;
