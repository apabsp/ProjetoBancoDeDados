import { useState, useEffect } from "react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";

export function InserirEmprestimo() {
  const [formData, setFormData] = useState({
    fkExemplar: "",
    fkCliente: "",
    dataEmprestimo: "",
    dataPrevistaDev: "",  // Data prevista de devolução que o usuário pode inserir
    dataDevolucao: null,   // Data de devolução começa como null, não será inserida pelo usuário
    id: "",                // ID gerado automaticamente
  });

  const [errors, setErrors] = useState({
    fkExemplar: "",
    fkCliente: "",
  });

  useEffect(() => {
    // Definir a data do empréstimo para a data e hora atual
    const now = new Date().toISOString().slice(0, 16); // Formato YYYY-MM-DDTHH:mm
    setFormData((prev) => ({
      ...prev,
      dataEmprestimo: now,
    }));

    // Gerar um ID automaticamente (exemplo de ID aleatório)
    const generatedId = `EMP-${Math.floor(Math.random() * 10000)}`;
    setFormData((prev) => ({
      ...prev,
      id: generatedId,
    }));
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;
    setFormData((prev) => ({ ...prev, [id]: value }));
  };

  const validateField = async (id: string, value: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/${id}/exists/${value}`);
      const exists = await response.json();

      setErrors((prev) => ({
        ...prev,
        [id]: exists ? "" : `${id === "fkExemplar" ? "Exemplar" : "Cliente"} não encontrado.`,
      }));
    } catch (error) {
      console.error("Erro na validação:", error);
    }
  };

  // Função para salvar os dados
  const handleSave = () => {
    // Aqui, você pode salvar ou fazer qualquer outra lógica necessária, com dataDevolucao já como null
    console.log(formData);
  };

  return (
    <div className="gap-5">
      <div className="flex flex-col items-center m-5 mb-1 gap-4">
        <div className="flex flex-col items-start w-full gap-2">
          <p className="text-xl ml-2">Exemplar</p>
          <Input
            id="fkExemplar"
            placeholder="Digite o id do exemplar"
            className="mr-2 placeholder:text-xl"
            value={formData.fkExemplar}
            onChange={handleChange}
            onBlur={(e) => validateField("fkExemplar", e.target.value)}
          />
          {errors.fkExemplar && <p className="text-red-500">{errors.fkExemplar}</p>}
        </div>

        <div className="flex flex-col items-start w-full gap-2">
          <p className="text-xl ml-2">Cliente</p>
          <Input
            id="fkCliente"
            placeholder="Digite o id do cliente"
            className="mr-2 placeholder:text-xl"
            value={formData.fkCliente}
            onChange={handleChange}
            onBlur={(e) => validateField("fkCliente", e.target.value)}
          />
          {errors.fkCliente && <p className="text-red-500">{errors.fkCliente}</p>}
        </div>

        <div className="flex flex-col items-start w-full gap-2">
          <p className="text-xl ml-2">Data prevista da devolução</p>
          <Input
            type="datetime-local"
            id="dataPrevistaDev"
            className="mr-2 placeholder:text-xl"
            value={formData.dataPrevistaDev}
            onChange={handleChange}
          />
        </div>

        {/* A data de devolução não será inserida pelo usuário, ela vai ser null automaticamente */}
      </div>

      <div className="flex justify-end m-4 mb-2">
        <Button
          className="p-4 bg-gray-200 hover:bg-gray-300 text-black"
          onClick={handleSave}  // Chamando a função de salvar
        >
          Salvar
        </Button>
      </div>
    </div>
  );
}

export default InserirEmprestimo;
