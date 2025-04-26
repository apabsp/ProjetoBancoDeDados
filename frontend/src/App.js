import React from 'react';
import ObraForm from './components/ObraForm';
import EditoraForm from './components/EditoraForm';
import AutorForm from './components/AutorForm';
import VisualizarObras from './components/VisualizarObras';
import EmprestimoSection from './components/EmprestimoSection';
import EmprestimoUpdateForm from './components/EmprestimoUpdateForm.jsx';
import PessoaForm from './components/PessoaForm.jsx';
import ObraList from './components/ObraList.jsx';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Register from './components/user/Register'; 

export default function App() {
  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <Router>
      <Routes>
        <Route path="/register" element={<Register />} />
        {/* You can add more routes like Login, Home, etc. */}
      </Routes>
      </Router>
      <div className="max-w-4xl mx-auto space-y-8">
        <h1 className="text-3xl font-bold text-center text-indigo-600">📚 Biblioteca App</h1>
        <div className="bg-red-200 p-4">
          Ainda estamos em construção! Reporte qualquer bugs 
        </div>
        <div className = "grid gap-6 border-spacing-20">
          <EmprestimoSection />
          <EmprestimoUpdateForm />
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <ObraForm />
          <ObraList />
          <EditoraForm />
          <AutorForm />
        </div>
        <VisualizarObras />
        <PessoaForm />

      </div>
    </div>
  );
}

