import React from 'react';
import DbInitSection from './components/DbInitSection';
import ObraForm from './components/ObraForm';
import EditoraForm from './components/EditoraForm';
import AutorForm from './components/AutorForm';
import VinculoSection from './components/VinculoSection';
import VisualizarObras from './components/VisualizarObras';

export default function App() {
  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <div className="max-w-4xl mx-auto space-y-8">
        <h1 className="text-3xl font-bold text-center text-indigo-600">📚 Biblioteca App</h1>
        <div className="bg-red-200 p-4">
          Error message!
        </div>
        <DbInitSection />
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <ObraForm />
          <EditoraForm />
          <AutorForm />
        </div>
        <VinculoSection />
        <VisualizarObras />
      </div>
    </div>
  );
}

