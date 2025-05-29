import { Input } from "../ui/input";
import { Search } from "lucide-react";
import { useState, useEffect } from "react";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "../ui/table";
import { Button } from "../ui/button";
import InserirCliente from "./InserirCliente";
import AtualizarCliente from "./AtualizarCliente";
import DeletarCliente from "./DeletarCliente";

interface Cliente {
  id: string;
  historico: string;
  fkPessoaId: string;
  nomePessoa: string; // Added this field
}

export function Cliente() {
  const [activeContent, setActiveContent] = useState<string>("");
  const [activeButton, setActiveButton] = useState<string>("");
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    if (activeContent === "visualizar") {
      fetchClientes();
    }
  }, [activeContent]);

  const fetchClientes = () => {
    fetch("http://localhost:8080/api/cliente/visualizar")
      .then(res => res.json())
      .then((data: Cliente[] | { content: Cliente[] }) => {
        const clientesData = Array.isArray(data) ? data : 
                             Array.isArray(data?.content) ? data.content : [];
        setClientes(clientesData);
      })
      .catch(error => {
        console.error("Erro ao buscar clientes:", error);
      });
  };

  const handleSearch = () => {
    if (!searchTerm.trim()) {
      fetchClientes();
      return;
    }

    const filtered = clientes.filter(cliente =>
      cliente.nomePessoa.toLowerCase().includes(searchTerm.toLowerCase()) ||
      cliente.id.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setClientes(filtered);
  };

  const renderContent = () => {
    switch (activeContent) {
      case "visualizar":
        return (
          <div>
            <div className="flex items-center m-5 mb-1">
              <Input 
                placeholder="Buscar por nome ou ID" 
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
                    <TableHead>ID</TableHead>
                    <TableHead>Nome</TableHead>
                    <TableHead>Histórico</TableHead>
                    <TableHead>ID Pessoa</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {clientes.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={4} className="text-center">
                        Nenhum cliente encontrado
                      </TableCell>
                    </TableRow>
                  ) : (
                    clientes.map((cliente) => (
                      <TableRow key={cliente.id}>
                        <TableCell className="font-medium">{cliente.id}</TableCell>
                        <TableCell>{cliente.nomePessoa}</TableCell>
                        <TableCell>{cliente.historico}</TableCell>
                        <TableCell>{cliente.fkPessoaId}</TableCell>
                      </TableRow>
                    ))
                  )}
                </TableBody>
              </Table>
            </div>
          </div>
        );

      case "inserir":
        return <InserirCliente />;
        
      case "atualizar":
        return <AtualizarCliente />;
        
      case "deletar":
        return <DeletarCliente />;
        
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
          <AccordionTrigger className="text-3xl font-medium text-black Jost">Clientes</AccordionTrigger>
          <AccordionContent className="bg-gray-100 rounded p-3">
            <div className="flex justify-evenly flex-wrap gap-4">
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
            <div className="mt-5">{renderContent()}</div>
          </AccordionContent>
        </AccordionItem>
      </Accordion>
    </div>
  );
}

export default Cliente;