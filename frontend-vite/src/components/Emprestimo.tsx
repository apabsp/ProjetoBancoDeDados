
import { Input } from "./ui/input";
import { Search } from "lucide-react";
import { useState } from "react";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow } from "./ui/table";
import InserirEmprestimo from "./InserirEmprestimo";
import { Button } from "./ui/button";

export function Emprestimo() {
  // Estado para controlar o conteúdo que será exibido e o botão ativo
  const [activeContent, setActiveContent] = useState<string>("");
  const [activeButton, setActiveButton] = useState<string>("");

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
            <div className="m-5"><Table>
              <TableHeader>
                <TableRow>
                  <TableHead >ID</TableHead>
                  <TableHead>Hoarário do emprestimo</TableHead>
                  <TableHead>Data do emprestimo</TableHead>
                  <TableHead>Data prevista da devolução</TableHead>
                  <TableHead>Data da devolução</TableHead>
                  <TableHead>ID Exemplar / Nome</TableHead>
                  <TableHead>ID Cliente / Nome</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                <TableRow>
                  <TableCell className="font-medium">1234</TableCell>
                  <TableCell>12:46</TableCell>
                  <TableCell>22/04/2025</TableCell>
                  <TableCell>02/05/2025</TableCell>
                  <TableCell>-</TableCell>
                  <TableCell>1A2B Exemplar 1</TableCell>
                  <TableCell>3C4D Cliente 1</TableCell>
                </TableRow>
              </TableBody>
            </Table>
            </div>
          </div>
        );
      case "inserir":
        return (
          <InserirEmprestimo/>
        );
      case "atualizar":
        return (
          <div>
            <div className="flex items-center m-5 mb-1">
              <Input placeholder="Digite o id do empréstimo" className="mr-2 placeholder:text-xl" />
              <Button className="p-2 bg-gray-200 hover:bg-gray-300">
                <Search size={20} className="text-black" />
              </Button>
            </div>
            <div className="gap-5">
              <div className="flex flex-col items-center m-5 mb-1 gap-4">
                <div className="flex flex-col items-start w-full gap-2">
                  <p className="text-xl ml-2">Exemplar</p>
                  <Input placeholder="Digite id do exemplar" className="mr-2 placeholder:text-xl" />
                </div>
                <div className="flex flex-col items-start w-full gap-2">
                  <p className="text-xl ml-2">Cliente</p>
                  <Input placeholder="Digite id do cliente" className="mr-2 placeholder:text-xl" />
                </div>
                <div className="flex flex-col items-start w-full gap-2">
                  <p className="text-xl ml-2">Data do empréstimo</p>
                  <Input
                    type="datetime-local"
                    placeholder="Digite a data do empréstimo 'dd-mm-aaaa'"
                    className="mr-2 placeholder:text-xl"
                  />
                </div>
                <div className="flex flex-col items-start w-full gap-2">
                  <p className="text-xl ml-2">Data prevista da devolução</p>
                  <Input
                    type="datetime-local"
                    placeholder="Digite a data prevista de devolução 'dd-mm-aaaa'"
                    className="mr-2 placeholder:text-xl"
                  />
                </div>
                <div className="flex flex-col items-start w-full gap-2">
                  <p className="text-xl ml-2">Data da devolução</p>
                  <Input
                    type="datetime-local"
                    placeholder="Digite a data prevista de devolução 'dd-mm-aaaa'"
                    className="mr-2 placeholder:text-xl"
                  /></div>
              </div>
              <div className="flex justify-end m-4 mb-2">
                <Button className="p-4 bg-gray-200 hover:bg-gray-300 text-black">Salvar</Button>
              </div>
            </div>
          </div>
        );
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
            <div className="mt-5">
              {renderContent()}
            </div>
          </AccordionContent>
        </AccordionItem>
      </Accordion>
    </div>
  );
}

export default Emprestimo;
