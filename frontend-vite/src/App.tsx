import './App.css'
import { SetStateAction, useState } from 'react';
import Exemplar from './components/Exemplar';
import Emprestimo from './components/Emprestimo';
import Obra from './components/ObraComponents/Obra';
import Cliente from './components/ClienteComponents/Cliente';
//import { Edicao } from './components/EdicaoComponents/Edicao';

function App() {
  // Criação de um estado para controlar o componente a ser exibido
  const [activeComponent, setActiveComponent] = useState('materiais');

  // Função para alterar o componente ativo
  const handleHeaderClick = (component: SetStateAction<string>) => {
    setActiveComponent(component);
  };

  // Função para adicionar o sublinhado ao item ativo
  const getHeaderClass = (component: string) => {
    return activeComponent === component ? 'underline' : '';
  };

  return (
    <>
      <header className="w-screen h-24 flex bg-gray-200 p-4 justify-evenly items-center fixed top-0 left-0 z-10">
        <p 
          className={`text-3xl font-medium text-black Jost cursor-pointer ${getHeaderClass('materiais')}`}
          onClick={() => handleHeaderClick('materiais')}
        >
          Materiais
        </p>
        <p 
          className={`text-3xl font-medium text-black Jost cursor-pointer ${getHeaderClass('clientes')}`}
          onClick={() => handleHeaderClick('clientes')}
        >
          Clientes
        </p>
        <p 
          className={`text-3xl font-medium text-black Jost cursor-pointer ${getHeaderClass('funcionarios')}`}
          onClick={() => handleHeaderClick('funcionarios')}
        >
          Funcionários
        </p>
      </header>

      <div className="h-screen w-screen grid grid-cols-5 gap-4 m-6 mt-32">
        <div className="col-start-2 col-span-3">
          {activeComponent === 'materiais' && <div className='g-4'><Exemplar /></div>}
          {activeComponent === 'materiais' && <div><Obra/></div>}
          {activeComponent === 'clientes' && <div><Emprestimo/></div>}
          {activeComponent === 'clientes' && <div><Cliente/> </div>}
          {activeComponent === 'funcionarios' && <div>Em construção</div>}
        </div>
      </div>
    </>
  );
}

export default App;
