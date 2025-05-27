import { useEffect, useState } from "react";
import { Bar } from "react-chartjs-2";
import { Chart as ChartJS, BarElement, CategoryScale, LinearScale, Tooltip, Legend } from "chart.js";
import { Button } from "../ui/button";
import { ReloadIcon } from "@radix-ui/react-icons";

// Register Chart.js components
ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend);

export function EmprestimosPorPessoa() {
  const [data, setData] = useState<Record<string, number>>({});
  const [erro, setErro] = useState("");
  const [loading, setLoading] = useState(false);

  const fetchEmprestimos = () => {
    setLoading(true);
    setErro("");
    fetch("http://localhost:8080/api/emprestimo/emprestimos-por-cliente")
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao buscar dados");
        return res.json();
      })
      .then((json) => setData(json))
      .catch((err) => setErro(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchEmprestimos();
  }, []);

  const nomes = Object.keys(data);
  const totais = Object.values(data);

  const chartData = {
    labels: nomes,
    datasets: [
      {
        label: "Total de Empréstimos",
        data: totais,
        backgroundColor: "rgba(59, 130, 246, 0.7)", 
        borderColor: "rgba(59, 130, 246, 1)",
        borderWidth: 1,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    scales: {
      y: {
        beginAtZero: true,
      },
    },
  };

  return (
    <div className="m-6">
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold">Empréstimos por Pessoa</h1>
        <Button onClick={fetchEmprestimos} className="flex items-center gap-2">
          <ReloadIcon className={loading ? "animate-spin" : ""} />
          Recarregar
        </Button>
      </div>

      {erro && <p className="text-red-600 mb-4">{erro}</p>}

      {!erro && nomes.length > 0 && (
        <Bar data={chartData} options={chartOptions} />
      )}

      {!erro && nomes.length === 0 && !loading && (
        <p className="text-gray-600">Nenhum dado encontrado.</p>
      )}
    </div>
  );
}

export default EmprestimosPorPessoa;
