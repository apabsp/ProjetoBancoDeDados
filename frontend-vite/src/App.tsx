import './App.css'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Register from './components/user/register';
import Login from './components/user/Login';
import AddEmprestimoButton from './components/AddEmprestimoButton';
import Exemplar from './components/Exemplar';

function App() {
  return (
    <>
      <header className="w-screen h-24 flex bg-gray-200 p-4 justify-evenly items-center fixed top-0 left-0 z-10">
        <p className="text-3xl font-medium text-black Jost">Materiais</p>
        <p className="text-3xl font-medium text-black Jost">Clientes</p>
        <p className="text-3xl font-medium text-black Jost">Funcionários</p>
      </header>

      <Router>
        <Routes>
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
        </Routes>
      </Router>

      <div className="h-screen w-screen grid grid-cols-5 gap-4 m-6 mt-32">
        <div className="col-start-2 col-span-3">
          <Exemplar />
        </div>
      </div>
    </>
  )
}

export default App;
