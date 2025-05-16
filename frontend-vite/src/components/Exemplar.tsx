import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion"
import { Button } from "./ui/button";

export function Exemplar() {

  return (

    <div className="">
      <Accordion type="single" collapsible>
        <AccordionItem value="item-1" className="bg-gray-200 rounded-lg p-3 pl-5 pr-5">
          <AccordionTrigger className="text-3xl font-medium text-black Jost">Exemplar</AccordionTrigger>
          <AccordionContent className="bg-gray-100 rounded p-3">
            <div className="flex justify-evenly">
              <Button className="bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300">Visualizar</Button>
              <Button className="bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300">Inserir</Button>
              <Button className="bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300">Atualizar</Button>
              <Button className="bg-gray-200 text-black text-2xl p-7 hover:bg-gray-300">Deletar</Button>
            </div>
          </AccordionContent>
        </AccordionItem>
      </Accordion>
    </div>
  );
}

export default Exemplar;