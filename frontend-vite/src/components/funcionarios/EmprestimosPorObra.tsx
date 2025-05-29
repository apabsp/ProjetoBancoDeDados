import { useEffect, useState } from "react";
import { Doughnut } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Button } from "../ui/button";
import { ReloadIcon } from "@radix-ui/react-icons";

ChartJS.register(ArcElement, Tooltip, Legend);

interface EmprestimoPorObra {
  titulo: string;
  periodo: string;
  total: number;
}

// Color palette - different colors for each segment
const COLOR_PALETTE = [
  'rgba(34, 197, 94, 0.7)',  // green
  'rgba(234, 179, 8, 0.7)',  // yellow
  'rgba(6, 182, 212, 0.7)',  // cyan
  'rgba(249, 115, 22, 0.7)', // orange
  'rgba(139, 92, 246, 0.7)', // purple
  'rgba(236, 72, 153, 0.7)', // pink
  'rgba(220, 38, 38, 0.7)',  // red
  'rgba(101, 163, 13, 0.7)', // lime
];

const BORDER_COLORS = COLOR_PALETTE.map(color => color.replace('0.7', '1'));

export function EmprestimosPorObra() {
  const [dados, setDados] = useState<EmprestimoPorObra[]>([]);
  const [erro, setErro] = useState("");
  const [loading, setLoading] = useState(false);
  const [tipo, setTipo] = useState<"mensal" | "anual">("mensal");

  const fetchDados = () => {
    setLoading(true);
    setErro("");
    fetch(`http://localhost:8080/api/emprestimo/emprestimos-por-obra?tipo=${tipo}`)
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao buscar dados");
        return res.json();
      })
      .then((json) => setDados(json))
      .catch((err) => setErro(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchDados();
  }, [tipo]);

  // Agrupar totais por obra
  const totaisPorObra = dados.reduce((acc, { titulo, total }) => {
    acc[titulo] = (acc[titulo] || 0) + total;
    return acc;
  }, {} as Record<string, number>);

  const obras = Object.keys(totaisPorObra);
  const valores = Object.values(totaisPorObra);

  const chartData = {
    labels: obras,
    datasets: [{
      data: valores,
      backgroundColor: obras.map((_, i) => COLOR_PALETTE[i % COLOR_PALETTE.length]),
      borderColor: obras.map((_, i) => BORDER_COLORS[i % BORDER_COLORS.length]),
      borderWidth: 1,
    }]
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        callbacks: {
          label: (context: any) => {
            const label = context.label || '';
            const value = context.raw || 0;
            const total = context.dataset.data.reduce((a: number, b: number) => a + b, 0);
            const percentage = Math.round((value / total) * 100);
            return `${label}: ${value} (${percentage}%)`;
          }
        }
      }
    },
    cutout: '65%',
  };

  return (
    <div className="m-6 w-[300px] h-[200px] p-4 bg-white rounded-2xl">
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold">Empréstimos por Obra</h1>
        <div className="flex gap-2 items-center">
          <select
            value={tipo}
            onChange={(e) => setTipo(e.target.value as "mensal" | "anual")}
            className="border px-2 py-1 rounded text-sm"
          >
            <option value="mensal">Mensal</option>
            <option value="anual">Anual</option>
          </select>
          
          <Button 
            onClick={fetchDados} 
            size="icon" 
            variant="ghost" 
            className="h-8 w-8"
          >
            <ReloadIcon className={loading ? "animate-spin" : ""} />
          </Button>
        </div>
      </div>

      {erro && <p className="text-red-600 text-sm mb-2">{erro}</p>}

      <div className="h-[100px]">
        {!erro && dados.length > 0 && <Doughnut data={chartData} options={chartOptions} />}
        {!erro && dados.length === 0 && !loading && (
          <p className="text-gray-600 text-sm">Nenhum dado encontrado.</p>
        )}
      </div>
    </div>
  );
}