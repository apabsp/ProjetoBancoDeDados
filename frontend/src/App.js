import React, { useState } from 'react';
import ObraInsertForm from './components/ObraInsertForm';
import './App.css';

function App() {

  const [message, setMessage] = useState("Clique no botão para criar a database!");
  const [loading, setLoading] = useState(false);

  const recreateDatabase = async () => {
    setLoading(true);

    try{
      const response = await fetch("http://localhost:8080/api/init", {
        method: "POST",
      });

      if (!response.ok) throw new Error("Something went wrong when creating database");

      const text = await response.text();
      setMessage(`Success: ${text}`);
    } catch (err) {
      setMessage(`Error: ${err.message}`);
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
  
      if (!response.ok) throw new Error("Error creating Obra table");
  
      const text = await response.text();
      setMessage(`Success: ${text}`);
    } catch (err) {
      setMessage(`Error: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1 className="title">📚 Biblioteca App</h1>
        <button className="action-button" onClick={recreateDatabase} disabled={loading}>
          {loading ? "Criando..." : "🔁 Criar Database"}
        </button>
        <p className="message">{message}</p>
        <h3 className="section-title">Cadastro de Obras</h3>
        <ObraInsertForm />
      </header>

    </div>
  );
}

export default App;
