import { Input } from "./ui/input";
import { Search } from "lucide-react";
import { useState } from "react";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "./ui/table";
import InserirEmprestimo from "./InserirEmprestimo";
import { Button } from "./ui/button";
import AtualizarEmprestimo from "./AtualizarEmprestimo";

export function Emprestimo() {
  // Estado para controlar o conteúdo que será exibido e o botão ativo
  const [activeContent, setActiveContent] = useState<string>("");
  const [activeButton, setActiveButton] = useState<string>("");

  // Estado para guardar os empréstimos — começa como array vazio para evitar erros
  const [emprestimos, setEmprestimos] = useState<any[]>([]);

  // Função para renderizar o conteúdo de acordo com a ação
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
                    <TableHead>Horário do empréstimo</TableHead>
                    <TableHead>Data do empréstimo</TableHead>
                    <TableHead>Data prevista da devolução</TableHead>
                    <TableHead>Data da devolução</TableHead>
                    <TableHead>ID Exemplar / Nome</TableHead>
                    <TableHead>ID Cliente / Nome</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {emprestimos.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={7} className="text-center">
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
                          <TableCell>{dataEmprestimo ? dataEmprestimo.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }) : "-"}</TableCell>
                          <TableCell>{dataEmprestimo ? dataEmprestimo.toLocaleDateString() : "-"}</TableCell>
                          <TableCell>{dataPrevistaDev ? dataPrevistaDev.toLocaleDateString() : "-"}</TableCell>
                          <TableCell>{dataDevolucao ? dataDevolucao.toLocaleDateString() : "-"}</TableCell>
                          <TableCell>{emprestimo.fkExemplar} / {emprestimo.nomeExemplar}</TableCell>
                          <TableCell>{emprestimo.fkCliente} / {emprestimo.nomeCliente}</TableCell>
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
        return (
          <div className="gap-5">
            <div className="flex items-center m-5 mb-1">
              <Input placeholder="Digite o id do empréstimo" className="mr-2 placeholder:text-xl" />
              <Button className="p-2 bg-gray-200 hover:bg-gray-300">
                <Search size={20} className="text-black" />
              </Button>
            </div>
            <div className="flex justify-end m-4 mb-2">
              <Button className="p-4 bg-gray-200 hover:bg-gray-300 text-black">Deletar</Button>
            </div>
          </div>
        );
      default:
        return null;
    }
  };

  // Função para aplicar a borda preta ao botão ativo
  const getButtonClass = (button: string) => {
    return activeButton === button ? "border-2 border-black" : "";
  };

  return (
    <div className="">
      <Accordion type="single" collapsible>
        <AccordionItem value="item-1" className="bg-gray-200 rounded-lg p-3 pl-5 pr-5">
          <AccordionTrigger className="text-3xl font-medium text-black Jost">Empréstimo</AccordionTrigger>
          <AccordionContent className="bg-gray-100 rounded p-3">
            {/* Botões para alternar o conteúdo */}
            <div className="flex justify-evenly">
              <Button
                className={`bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300 ${getButtonClass("visualizar")}`}
                onClick={() => {
                  setActiveContent("visualizar");
                  setActiveButton("visualizar");

                  fetch("http://localhost:8080/api/emprestimo/visualizar")
                  .then(res => res.json())
                  .then(data => {
                    console.log("Dados recebidos do backend:", data);
                    if (Array.isArray(data)) {
                      setEmprestimos(data);
                    } else if (Array.isArray(data.content)) {
                      setEmprestimos(data.content);
                    } else {
                      setEmprestimos([]);
                      console.warn("Formato de dados inesperado", data);
                    }
                  })
                  .catch(error => {
                    console.error("Erro ao buscar empréstimos:", error);
                  });
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

            {/* Aqui o conteúdo muda dependendo da ação selecionada */}
            <div className="mt-5">{renderContent()}</div>
          </AccordionContent>
        </AccordionItem>
      </Accordion>
    </div>
  );
}

export default Emprestimo;
