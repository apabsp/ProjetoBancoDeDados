import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion"

export function Exemplar() {

  return (

    <div className="">
      <Accordion type="single" collapsible>
        <AccordionItem value="item-1" className="bg-blue border-0">
          <AccordionTrigger className="text-3xl font-medium text-black Jost">Exemplar</AccordionTrigger>
          <AccordionContent>
            Yes. It adheres to the WAI-ARIA design pattern.
          </AccordionContent>
        </AccordionItem>
      </Accordion>
    </div>
  );
}

export default Exemplar;