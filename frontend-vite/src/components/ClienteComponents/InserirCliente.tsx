import { useState } from "react";
import { Input } from "../ui/input";
import { Button } from "../ui/button";

function InserirCliente() {
  const [nomePessoa, setNomePessoa] = useState("");
  const [historico, sethistorico] = useState("");
  const [mensagem, setMensagem] = useState("");

  const handleSubmit = async () => {
    try {
      // Single API call that handles both Pessoa and Cliente creation
      const response = await fetch("http://localhost:8080/api/cliente/pessoaInserir", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          nomePessoa: nomePessoa,
          historico: historico
        }),
      });

      const result = await response.text();
      setMensagem(result);
      
      // Clear form
      setNomePessoa("");
      sethistorico("");
      
    } catch (error) {
      console.error("Erro ao cadastrar:", error);
      setMensagem("Erro ao cadastrar cliente.");
    }
  };

  return (
    <div className="flex flex-col items-center m-5 gap-4 w-full max-w-md">
      <div className="w-full">
        <p className="text-xl mb-1">Nome da Pessoa*</p>
        <Input
          value={nomePessoa}
          onChange={(e) => setNomePessoa(e.target.value)}
          placeholder="Digite o nome completo"
        />
      </div>

      <div className="w-full">
        <p className="text-xl mb-1">Informações do Cliente</p>
        <Input
          value={historico}
          onChange={(e) => sethistorico(e.target.value)}
          placeholder="Digite o histórico (opcional)"
        />
      </div>

      <Button
        className="mt-4 p-4 bg-gray-200 hover:bg-gray-300 text-black"
        onClick={handleSubmit}
        disabled={!nomePessoa.trim()}
      >
        Cadastrar
      </Button>

      {mensagem && (
        <p className={`text-md mt-2 ${mensagem.includes("Erro") ? "text-red-600" : "text-green-600"}`}>
          {mensagem}
        </p>
      )}
    </div>
  );
}

export default InserirCliente;