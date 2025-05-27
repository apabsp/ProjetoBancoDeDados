import { Input } from "./ui/input";
import { Search } from "lucide-react";
import { useState } from "react";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "./ui/table";
import InserirEmprestimo from "./InserirEmprestimo";
import { Button } from "./ui/button";
import AtualizarEmprestimo from "./AtualizarEmprestimo";
import DeletarEmprestimo from "./DeletarEmprestimo";

export function Emprestimo() {
  const [activeContent, setActiveContent] = useState<string>("");
  const [activeButton, setActiveButton] = useState<string>("");
  const [emprestimos, setEmprestimos] = useState<any[]>([]);

  const checkDaysUntilReturn = async (idEmprestimo: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/emprestimo/dias-ate-devolucao?idEmprestimo=${idEmprestimo}`);
      const result = await response.text();
      const days = parseInt(result);
      
      if (!isNaN(days)) {
        setEmprestimos(prevEmprestimos => 
          prevEmprestimos.map(emp => 
            emp.id === idEmprestimo 
              ? { ...emp, daysUntilReturn: days } 
              : emp
          )
        );
      } else {
        console.error(result);
        // Opcional: Mostrar mensagem de erro para o usuário
      }
    } catch (error) {
      console.error("Erro ao verificar dias até devolução:", error);
    }
  };

  const renderContent = () => {
    switch (activeContent) {
      case "visualizar":
        return (
          <div>
            <div className="flex items-center m-5 mb-1">
              <Input placeholder="Digite o id do emprestimo" className="mr-2 placeholder:text-xl" />
              <Button className="p-2 bg-gray-200 hover:bg-gray-300">
                <Search size={20} className="text-black" />
              </Button>
            </div>
            <div className="m-5">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>ID</TableHead>
                    <TableHead>Horário</TableHead>
                    <TableHead>Data Empréstimo</TableHead>
                    <TableHead>Previsão Devolução</TableHead>
                    <TableHead>Data Devolução</TableHead>
                    <TableHead>Exemplar</TableHead>
                    <TableHead>Cliente</TableHead>
                    <TableHead>Status Devolução</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {emprestimos.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={8} className="text-center">
                        Nenhum empréstimo encontrado
                      </TableCell>
                    </TableRow>
                  ) : (
                    emprestimos.map((emprestimo: any) => {
                      const dataEmprestimo = emprestimo.dataEmprestimo ? new Date(emprestimo.dataEmprestimo) : null;
                      const dataPrevistaDev = emprestimo.dataPrevistaDev ? new Date(emprestimo.dataPrevistaDev) : null;
                      const dataDevolucao = emprestimo.dataDevolucao ? new Date(emprestimo.dataDevolucao) : null;

                      return (
                        <TableRow key={emprestimo.id}>
                          <TableCell className="font-medium">{emprestimo.id}</TableCell>
                          <TableCell>{dataEmprestimo?.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }) || "-"}</TableCell>
                          <TableCell>{dataEmprestimo?.toLocaleDateString() || "-"}</TableCell>
                          <TableCell>{dataPrevistaDev?.toLocaleDateString() || "-"}</TableCell>
                          <TableCell>{dataDevolucao?.toLocaleDateString() || "-"}</TableCell>
                          <TableCell>{emprestimo.fkExemplar} / {emprestimo.nomeObra}</TableCell>
                          <TableCell>{emprestimo.fkCliente} / {emprestimo.nomeCliente}</TableCell>
                          <TableCell className="flex items-center">
                            <Button 
                              onClick={() => checkDaysUntilReturn(emprestimo.id)}
                              className="bg-blue-500 hover:bg-blue-600 text-white"
                              size="sm"
                            >
                              Verificar
                            </Button>
                            {emprestimo.daysUntilReturn !== undefined && (
                              <span className={`ml-2 ${
                                emprestimo.daysUntilReturn < 0 ? "text-red-500 font-medium" :
                                emprestimo.daysUntilReturn === 0 ? "text-orange-500 font-medium" :
                                "text-green-600"
                              }`}>
                                {emprestimo.daysUntilReturn > 0 
                                  ? `${emprestimo.daysUntilReturn} dias restantes`
                                  : emprestimo.daysUntilReturn === 0
                                    ? "Devolver hoje"
                                    : `${Math.abs(emprestimo.daysUntilReturn)} dias atrasado`}
                              </span>
                            )}
                          </TableCell>
                        </TableRow>
                      );
                    })
                  )}
                </TableBody>
              </Table>
            </div>
          </div>
        );
      case "inserir":
        return <InserirEmprestimo />;
      case "atualizar":
        return <AtualizarEmprestimo />;
      case "deletar":
        return <DeletarEmprestimo />;
      default:
        return null;
    }
  };

  const getButtonClass = (button: string) => {
    return activeButton === button ? "border-2 border-black" : "";
  };

  return (
    <div className="">
      <Accordion type="single" collapsible>
        <AccordionItem value="item-1" className="bg-gray-200 rounded-lg p-3 pl-5 pr-5">
          <AccordionTrigger className="text-3xl font-medium text-black Jost">Empréstimo</AccordionTrigger>
          <AccordionContent className="bg-gray-100 rounded p-3">
            <div className="flex justify-evenly">
              <Button
                className={`bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300 ${getButtonClass("visualizar")}`}
                onClick={() => {
                  setActiveContent("visualizar");
                  setActiveButton("visualizar");
                  fetch("http://localhost:8080/api/emprestimo/visualizar")
                    .then(res => res.json())
                    .then(data => {
                      if (Array.isArray(data)) {
                        setEmprestimos(data);
                      } else if (Array.isArray(data.content)) {
                        setEmprestimos(data.content);
                      } else {
                        setEmprestimos([]);
                      }
                    })
                    .catch(console.error);
                }}
              >
                Visualizar
              </Button>
              <Button
                className={`bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300 ${getButtonClass("inserir")}`}
                onClick={() => {
                  setActiveContent("inserir");
                  setActiveButton("inserir");
                }}
              >
                Inserir
              </Button>
              <Button
                className={`bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300 ${getButtonClass("atualizar")}`}
                onClick={() => {
                  setActiveContent("atualizar");
                  setActiveButton("atualizar");
                }}
              >
                Atualizar
              </Button>
              <Button
                className={`bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300 ${getButtonClass("deletar")}`}
                onClick={() => {
                  setActiveContent("deletar");
                  setActiveButton("deletar");
                }}
              >
                Deletar
              </Button>
            </div>
            <div className="mt-5">{renderContent()}</div>
          </AccordionContent>
        </AccordionItem>
      </Accordion>
    </div>
  );
}

export default Emprestimo;