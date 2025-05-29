import { useEffect, useState } from 'react';
import { Button } from '../ui/button';
import { ReloadIcon } from "@radix-ui/react-icons";

type FuncionarioData = {
  nome_funcionario: string;
  total_emprestimos: number;
};

export default function TopFuncionarioCard() {
  const [topFuncionario, setTopFuncionario] = useState<FuncionarioData | null>(null);
  const [erro, setErro] = useState('');
  const [loading, setLoading] = useState(false);
  const [tipo, setTipo] = useState<'ano' | 'mes'>('ano');
  const [dataSelecionada, setDataSelecionada] = useState<string>(() => {
    const now = new Date();
    return `${now.getFullYear()}`;
  });

  const buscarTopFuncionario = () => {
    if (!dataSelecionada) return;

    setLoading(true);
    setErro('');
    setTopFuncionario(null);

    const url = `http://localhost:8080/api/emprestimo/top-funcionario?tipo=${tipo}&agrupadoPor=${dataSelecionada}`;

    fetch(url)
      .then((res) => {
        if (!res.ok) throw new Error('Erro ao buscar top funcionário');
        return res.json();
      })
      .then((data) => {
        console.log('Top funcionário recebido:', data);
        setTopFuncionario(data);
      })
      .catch((err) => setErro(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    buscarTopFuncionario();
  }, [dataSelecionada, tipo]);

  return (
    <div className="p-4 w-[350px] bg-white rounded-2xl flex flex-col gap-2 ">
      <h3 className = "font-bold">Funcionário destaque </h3>
      <div className="flex gap-2 items-center">
        <select
          className="border rounded px-2 py-1 text-sm"
          value={tipo}
          onChange={(e) => {
            const value = e.target.value as 'ano' | 'mes';
            setTipo(value);
            const now = new Date();
            setDataSelecionada(
              value === 'ano'
                ? `${now.getFullYear()}`
                : `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
            );
          }}
        >
          <option value="ano">Ano</option>
          <option value="mes">Mês</option>
        </select>

        {tipo === 'ano' ? (
          <input
            type="number"
            min="1990"
            max="2099"
            step="1"
            className="border rounded px-2 py-1 text-sm w-[100px]"
            value={dataSelecionada}
            onChange={(e) => setDataSelecionada(e.target.value)}
          />
        ) : (
          <input
            type="month"
            className="border rounded px-2 py-1 text-sm"
            value={dataSelecionada}
            onChange={(e) => setDataSelecionada(e.target.value)}
          />
        )}

        <Button variant="secondary" onClick={buscarTopFuncionario}>
          <ReloadIcon />
        </Button>
      </div>

      <div className="flex flex-col mt-2">
        {erro && <div className="text-sm text-red-600">{erro}</div>}

        {!erro && topFuncionario ? (
          <>
            <h3 className="text-sm text-gray-500">
              Top Funcionário ({tipo === 'ano' ? 'Ano' : 'Mês'}: {dataSelecionada})
            </h3>
            <div className="text-lg font-bold text-gray-800">{topFuncionario.nome_funcionario}</div>
            <div className="text-sm text-gray-600">{topFuncionario.total_emprestimos} empréstimos</div>
          </>
        ) : !erro && !loading ? (
          <div className="text-sm text-gray-500">Nenhum dado disponível.</div>
        ) : null}
      </div>
    </div>
  );
}
