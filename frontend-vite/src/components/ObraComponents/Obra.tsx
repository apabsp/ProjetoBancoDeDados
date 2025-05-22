import { Input } from "../ui/input";
import { Search } from "lucide-react";
import { useState, useEffect } from "react";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "../ui/table";
import { Button } from "../ui/button";
import InserirObra from "./InserirObra";
import AtualizarObra from "./AtualizarObra";
import DeletarObra from "./DeletarObra";

interface Obra {
  cod_barras: string;
  titulo: string;
  ano_lanc: string | null;
}

export function Obra() {
  const [activeContent, setActiveContent] = useState<string>("");
  const [activeButton, setActiveButton] = useState<string>("");
  const [obras, setObras] = useState<Obra[]>([]);
  const [searchTerm, setSearchTerm] = useState("");

  // Fetch obras on initial load and when returning to view mode
  useEffect(() => {
    if (activeContent === "visualizar") {
      fetchObras();
    }
  }, [activeContent]);

  const fetchObras = () => {
    fetch("http://localhost:8080/api/obra/visualizar")
      .then(res => res.json())
      .then((data: Obra[] | { content: Obra[] }) => {
        const obrasData = Array.isArray(data) ? data : 
                         Array.isArray(data?.content) ? data.content : [];
        setObras(obrasData); // Só um verificador para aceitar tanto o content quanto o objeto
      })
      .catch(error => {
        console.error("Erro ao buscar obras:", error);
      });
  };

  const handleSearch = () => {
    if (!searchTerm.trim()) {
      fetchObras();
      return;
    }

    const filtered = obras.filter(obra =>
      obra.cod_barras.toLowerCase().includes(searchTerm.toLowerCase()) ||
      obra.titulo.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setObras(filtered);
  };

  const renderContent = () => {
    switch (activeContent) {
      case "visualizar":
        return (
          <div>
            <div className="flex items-center m-5 mb-1">
              <Input 
                placeholder="Buscar por título ou código" 
                className="mr-2 placeholder:text-xl"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
              />
              <Button 
                className="p-2 bg-gray-200 hover:bg-gray-300"
                onClick={handleSearch}
              >
                <Search size={20} className="text-black" />
              </Button>
            </div>
            <div className="m-5">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Código de Barras</TableHead>
                    <TableHead>Título</TableHead>
                    <TableHead>Ano de Lançamento</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {obras.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={3} className="text-center">
                        Nenhuma obra encontrada
                      </TableCell>
                    </TableRow>
                  ) : (
                    obras.map((obra) => (
                      <TableRow key={obra.cod_barras}>
                        <TableCell className="font-medium">{obra.cod_barras}</TableCell>
                        <TableCell>{obra.titulo}</TableCell>
                        <TableCell>
                          {obra.ano_lanc ? new Date(obra.ano_lanc).toLocaleDateString() : '-'}
                        </TableCell>
                      </TableRow>
                    ))
                  )}
                </TableBody>
              </Table>
            </div>
          </div>
        );

        
      case "inserir":
        return <InserirObra />;
        /*
      case "atualizar":

        return <AtualizarObra />;
      case "deletar":
        return <DeletarObra />;
        */
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
          <AccordionTrigger className="text-3xl font-medium text-black Jost">Obras</AccordionTrigger>
          <AccordionContent className="bg-gray-100 rounded p-3">
            <div className="flex justify-evenly flex-wrap gap-4">
              <Button
                className={`bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300 ${getButtonClass("visualizar")}`}
                onClick={() => setActiveContent("visualizar")}
              >
                Visualizar
              </Button>
              <Button
                className={`bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300 ${getButtonClass("inserir")}`}
                onClick={() => setActiveContent("inserir")}
              >
                Inserir
              </Button>
              <Button
                className={`bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300 ${getButtonClass("atualizar")}`}
                onClick={() => setActiveContent("atualizar")}
              >
                Atualizar
              </Button>
              <Button
                className={`bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300 ${getButtonClass("deletar")}`}
                onClick={() => setActiveContent("deletar")}
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

export default Obra;