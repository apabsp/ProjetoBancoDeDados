import logo from './logo.svg';
import './App.css';
import React, { useState } from 'react';
import EditoraForm from './components/EditoraForm';
import ObraForm from './components/ObraForm';

function App() {

  const [message, setMessage] = useState("Sample message");
  const [loading, setLoading] = useState(false);

  const recreateDatabase = async () => {
    setLoading(true);//We can have an animation playing while this is true

    try{
      const response = await fetch("http://localhost:8080/api/init", {
        method: "POST",
      });

      if (!response.ok) throw new Error("Something went wrong whhen creating database");

      const text = await response.text();
      setMessage(`Success: ${text}`);
    } catch (err) {
      setMessage(`Erro: ${err.message}`);
    } finally {
      setLoading(false);
    }
  }

  const recreateObraTable = async () => {
    setLoading(true);
  
    try {
      const response = await fetch("http://localhost:8080/api/init/obra", {
        method: "POST",
      });
  
      if (!response.ok) throw new Error("Erro ao criar tabela de obra");
  
      const text = await response.text();
      setMessage(`Sucesso: ${text}`);
    } catch (err) {
      setMessage(`Erro: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  

  return (
      <div className="App">
        <header className="App-header">
          <h1>📚 Biblioteca App</h1>
          <button onClick={recreateDatabase} disabled={loading}>
            {loading ? "Criando..." : "🔁 Criar Database"}
          </button>
          <p>{message}</p>
          <img src={logo} className="App-logo" alt="logo" />

          <button onClick={recreateObraTable} disabled={loading}>
            {loading ? "Criando..." : "📄 Criar Tabela Obra"}
          </button>
          
        </header>
        <h1>Cadastro de Editoras</h1>
        <EditoraForm />

        <h1>Cadastro de Obras</h1>
        <ObraForm />
      </div>
    );
  }


export default App;
