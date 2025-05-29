import { Input } from "./ui/input";
import { Search } from "lucide-react";
import { useState } from "react";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "./ui/table";
import { Button } from "./ui/button";
import InserirExemplar from "./InserirExemplar";
import AtualizarExemplar from "./AtualizarExemplar";
import DeletarExemplar from "./DeletarExemplar";

export function Exemplar() {
  const [activeContent, setActiveContent] = useState<string>("");
  const [activeButton, setActiveButton] = useState<string>("");
  const [exemplares, setExemplares] = useState<any[]>([]);

  const renderContent = () => {
    switch (activeContent) {
      case "visualizar":
        return (
          <div>
            <div className="flex items-center m-5 mb-1">
              <Input placeholder="Digite o ID do exemplar" className="mr-2 placeholder:text-xl" />
              <Button className="p-2 bg-gray-200 hover:bg-gray-300">
                <Search size={20} className="text-black" />
              </Button>
            </div>
            <div className="m-5">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>ID</TableHead>
                    <TableHead>Obra</TableHead>
                    <TableHead>Edição</TableHead>
                    <TableHead>Artigo</TableHead>
                    <TableHead>Estante (Prateleira / Número)</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {exemplares.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={4} className="text-center">
                        Nenhum exemplar encontrado
                      </TableCell>
                    </TableRow>
                  ) : (
                    exemplares.map((exemplar: any) => (
                      <TableRow key={exemplar.id}>
                        <TableCell className="font-medium">{exemplar.id}</TableCell>
                        <TableCell>{exemplar.nomeObra}</TableCell>
                        <TableCell>{exemplar.fkEdicao}</TableCell>
                        <TableCell>{exemplar.fkArtigo}</TableCell>
                        <TableCell>{exemplar.fkEstantePrateleira} / {exemplar.fkEstanteNumero}</TableCell>
                      </TableRow>
                    ))
                  )}
                </TableBody>
              </Table>
            </div>
          </div>
        );
      case "inserir":
        return <InserirExemplar />;
      case "atualizar":
        return <AtualizarExemplar />;
      case "deletar":
        return <DeletarExemplar />;
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
          <AccordionTrigger className="text-3xl font-medium text-black Jost">Exemplares</AccordionTrigger>
          <AccordionContent className="bg-gray-100 rounded p-3">
            <div className="flex justify-evenly flex-wrap gap-4">
              <Button
                className={`bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300 ${getButtonClass("visualizar")}`}
                onClick={() => {
                  setActiveContent("visualizar");
                  setActiveButton("visualizar");

                  fetch("http://localhost:8080/api/exemplar/visualizar")
                    .then(res => res.json())
                    .then(data => {
                      if (Array.isArray(data)) {
                        setExemplares(data);
                      } else if (Array.isArray(data.content)) {
                        setExemplares(data.content);
                      } else {
                        setExemplares([]);
                      }
                    })
                    .catch(error => {
                      console.error("Erro ao buscar exemplares:", error);
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

            <div className="mt-5">{renderContent()}</div>
          </AccordionContent>
        </AccordionItem>
      </Accordion>
    </div>
  );
}

export default Exemplar;
