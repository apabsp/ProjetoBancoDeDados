import { useState } from "react";
import { Input } from "./ui/input";
import { Button } from "./ui/button";

function InserirExemplar() {
  const [fkEdicao, setFkEdicao] = useState("");
  const [fkArtigo, setFkArtigo] = useState("");
  const [fkEstanteNumero, setFkEstanteNumero] = useState("");
  const [fkEstantePrateleira, setFkEstantePrateleira] = useState("");
  const [mensagem, setMensagem] = useState("");

  const handleSubmit = () => {
    const payload = {
      fkEdicao,
      fkArtigo,
      fkEstanteNumero,
      fkEstantePrateleira
    };

    fetch("http://localhost:8080/api/exemplar/inserir", {
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
        console.error("Erro ao inserir exemplar:", error);
        setMensagem("Erro ao inserir exemplar.");
      });
  };

  return (
    <div className="flex flex-col items-start m-5 gap-4">
      <div className="w-full">
        <p className="text-xl mb-1">ID da Edição</p>
        <Input
          value={fkEdicao}
          onChange={e => setFkEdicao(e.target.value)}
          placeholder="Digite o ID da edição"
        />
      </div>
      <div className="w-full">
        <p className="text-xl mb-1">ID do Artigo</p>
        <Input
          value={fkArtigo}
          onChange={e => setFkArtigo(e.target.value)}
          placeholder="Digite o ID do artigo"
        />
      </div>
      <div className="w-full">
        <p className="text-xl mb-1">Número da Estante</p>
        <Input
          value={fkEstanteNumero}
          onChange={e => setFkEstanteNumero(e.target.value)}
          placeholder="Digite o número da estante"
        />
      </div>
      <div className="w-full">
        <p className="text-xl mb-1">Prateleira da Estante</p>
        <Input
          value={fkEstantePrateleira}
          onChange={e => setFkEstantePrateleira(e.target.value)}
          placeholder="Digite a prateleira da estante"
        />
      </div>
      <Button className="mt-4 p-4 bg-gray-200 hover:bg-gray-300 text-black" onClick={handleSubmit}>
        Salvar
      </Button>
      {mensagem && <p className="text-md mt-2 text-green-600">{mensagem}</p>}
    </div>
  );
}

export default InserirExemplar;
