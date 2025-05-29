import { useEffect, useState } from "react";
import { Bar } from "react-chartjs-2";
import { Chart as ChartJS, BarElement, CategoryScale, LinearScale, Tooltip, Legend } from "chart.js";
import { Button } from "../ui/button";
import { ReloadIcon } from "@radix-ui/react-icons";

ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend);

export function EmprestimosPorPeriodo() {
  const [data, setData] = useState<Record<string, number>>({});
  const [erro, setErro] = useState("");
  const [loading, setLoading] = useState(false);
  const [tipo, setTipo] = useState<"mes" | "ano">("mes");

  const fetchEmprestimos = () => {
    setLoading(true);
    setErro("");
    fetch(`http://localhost:8080/api/emprestimo/emprestimos-por-periodo?tipo=${tipo}`)
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
  }, [tipo]);

  const labels = Object.keys(data);
  const valores = Object.values(data);

  const chartData = {
    labels,
    datasets: [
      {
        label: `Total de Empréstimos por ${tipo === "mes" ? "Mês" : "Ano"}`,
        data: valores,
        backgroundColor: "rgba(34, 197, 94, 0.7)",
        borderColor: "rgba(34, 197, 94, 1)",
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
    <div className="m-6 w-[300px] h-[200px] p-4 bg-white rounded-2xl">
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold">Empréstimos por Período</h1>
        <div className="flex gap-2 items-center">
          <select
            value={tipo}
            onChange={(e) => setTipo(e.target.value as "mes" | "ano")}
            className="border px-2 py-1 rounded"
          >
            <option value="mes">Agrupar por Mês</option>
            <option value="ano">Agrupar por Ano</option>
          </select>

          <Button onClick={fetchEmprestimos} className="flex items-center gap-2">
            <ReloadIcon className={loading ? "animate-spin" : ""} />
          </Button>
        </div>
      </div>

      {erro && <p className="text-red-600 mb-4">{erro}</p>}

      {!erro && labels.length > 0 && <Bar data={chartData} options={chartOptions} />}

      {!erro && labels.length === 0 && !loading && (
        <p className="text-gray-600">Nenhum dado encontrado.</p>
      )}
    </div>
  );
}

export default EmprestimosPorPeriodo;
