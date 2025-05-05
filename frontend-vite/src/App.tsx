import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { Button } from './components/ui/button'
import { app } from "./firebase";
import { getAuth } from "firebase/auth"; 
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Register from './components/user/register'
import Login from './components/user/Login';
import EmprestimoTable from './components/tables/EmprestimoTable'
import AddEmprestimoButton from './components/AddEmprestimoButton'
function App() {
  

  return (
    <>
      <header className="w-screen flex bg-blue-500 p-4 justify-center">
          <h1 className="text-3xl font-bold text-white oswald text-3x1">
            Bibliotecapp!
          </h1>
      </header>

        <Router>
        <Routes>
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login/>}/>
        </Routes>
        </Router>

      <div className="flex flex-col justify-center w-3/4 mx-auto">
        <div className="flex justify-between">
          <Button variant="outline">Empréstimos</Button>
          <Button variant="outline">Obras</Button>
          <Button variant="outline">Autores</Button>
          <Button variant="outline">Clientes</Button>
          <Button variant="outline">Funcionários</Button>
        </div>
        
        <EmprestimoTable/>

        <div className=  "flex justify-between">
          <AddEmprestimoButton/>
        </div>
      </div>
        

    
    </>
  )
}

export default App
