import { Button } from "@/components/ui/button"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useState } from "react"


export function AddEmprestimoButton() {

  const [formData, setFormData] = useState(
    {
      dataEmprestimo: "",
      dataPrevistaDev: "",
      fkExemplar: "",
      fkCliente: "",
      fkFuncionario: "",
    }
  )

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const {id, value} = e.target;
    setFormData(prev => ({...prev, [id]: value}))
  };

  const handleSubmit = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/emprestimo/inserir", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });
  
      console.log(formData);
      const text = await response.text();
  
      if (!response.ok || text.toLowerCase().includes("erro")) {
        alert("Erro ao criar empréstimo:\n" + text);
      } else {
        alert("Empréstimo criado com sucesso!");
        setFormData({
          dataEmprestimo: "",
          dataPrevistaDev: "",
          fkExemplar: "",
          fkCliente: "",
          fkFuncionario: "",
        });
      }
  
    } catch (error: any) {
      console.error("Erro na requisição:", error);
      alert("Erro na requisição: " + error.message);
    }
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="outline">Adicionar Empréstimo</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Adicionar Empréstimo</DialogTitle>
          <DialogDescription>
            Preencha os campos abaixo para registrar um novo empréstimo.
          </DialogDescription>
        </DialogHeader>

        <div className="grid gap-4 py-4">
          {[
            { label: "Data Empréstimo", id: "dataEmprestimo", type: "datetime-local" },
            { label: "Data Prevista Devolução", id: "dataPrevistaDev", type: "datetime-local" },
            { label: "ID do Exemplar", id: "fkExemplar", type: "text" },
            { label: "ID do Cliente", id: "fkCliente", type: "text" },
            { label: "ID do Funcionário", id: "fkFuncionario", type: "text" },
          ].map(({ label, id, type }) => (
            <div key={id} className="grid grid-cols-4 items-center gap-4">
              <Label htmlFor={id} className="text-right">{label}</Label>
              <Input
                id={id}
                type={type}
                value={formData[id as keyof typeof formData]}
                onChange={handleChange}
                className="col-span-3"
              />
            </div>
          ))}
        </div>

        <DialogFooter>
          <Button type="button" onClick={handleSubmit}>
            Salvar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

export default AddEmprestimoButton;