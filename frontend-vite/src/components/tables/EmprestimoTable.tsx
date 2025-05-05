import React, { useEffect, useState } from 'react';
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Trash2, Pencil } from "lucide-react";

type Emprestimo = {
  id: string;
  hora: string;
  dataPrevistaDev: string;
  dataDevolucao: string | null;
  dataEmprestimo: string;
  fkExemplar: string;
  fkCliente: string;
  fkFuncionario?: string;
};

const formatDate = (dateStr: string) => {
  const date = new Date(dateStr);
  return date.toLocaleString("pt-BR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const EmprestimoTable: React.FC = () => {
  const [rows, setRows] = useState<Emprestimo[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [editingEmprestimo, setEditingEmprestimo] = useState<Emprestimo | null>(null);
  const [novaDataDevolucao, setNovaDataDevolucao] = useState<string>("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/emprestimo/visualizar");
        if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
        const data: Emprestimo[] = await res.json();
        setRows(data);
      } catch (err) {
        console.error("Failed to fetch data:", err);
        setError(err instanceof Error ? err.message : 'Unknown error occurred');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleEdit = (emprestimo: Emprestimo) => {
    setEditingEmprestimo(emprestimo);
    setNovaDataDevolucao(emprestimo.dataDevolucao?.slice(0, 16) || "");
  };

  const handleSubmitEdit = async () => {
    if (!editingEmprestimo) return;

    try {
      const res = await fetch(`http://localhost:8080/api/emprestimo/alterar?id=${editingEmprestimo.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          ...editingEmprestimo,
          dataDevolucao: novaDataDevolucao || null,
        }),
      });

      const msg = await res.text();
      if (!res.ok) throw new Error(msg);

      alert("Empréstimo atualizado!");

      setRows(prev =>
        prev.map(r =>
          r.id === editingEmprestimo.id
            ? { ...r, dataDevolucao: novaDataDevolucao }
            : r
        )
      );

      setEditingEmprestimo(null);
    } catch (error: any) {
      alert("Erro ao atualizar: " + error.message);
    }
  };

  const handleDelete = async (id: string) => {
    const confirmed = window.confirm("Tem certeza que deseja excluir este empréstimo?");
    if (!confirmed) return;

    try {
      const res = await fetch(`http://localhost:8080/api/emprestimo/deletar?id=${id}`, {
        method: "DELETE",
      });

      const text = await res.text();
      if (!res.ok) throw new Error(`Erro ao deletar empréstimo: ${text}`);

      setRows(prev => prev.filter(row => row.id !== id));
      alert(text);
    } catch (error: any) {
      console.error("Erro ao excluir empréstimo:", error);
      alert("Erro ao excluir empréstimo: " + error.message);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        <strong>Erro: </strong>
        <span>{error}</span>
      </div>
    );
  }

  return (
    <>
      <div className="rounded-md border">
        <Table>
          <TableCaption className="text-lg font-medium">EMPRÉSTIMOS REGISTRADOS</TableCaption>
          <TableHeader>
            <TableRow>
              <TableHead className="w-[100px]">ID</TableHead>
              <TableHead>Data Empréstimo</TableHead>
              <TableHead>Previsão Devolução</TableHead>
              <TableHead>Data Devolução</TableHead>
              <TableHead>Exemplar</TableHead>
              <TableHead>Cliente</TableHead>
              {rows.some(row => row.fkFuncionario) && <TableHead>Funcionário</TableHead>}
              <TableHead>Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {rows.map((row) => (
              <TableRow key={row.id}>
                <TableCell className="font-medium">{row.id}</TableCell>
                <TableCell>{formatDate(row.dataEmprestimo)}</TableCell>
                <TableCell>{formatDate(row.dataPrevistaDev)}</TableCell>
                <TableCell>
                  {row.dataDevolucao ? formatDate(row.dataDevolucao) : 'Não devolvido'}
                </TableCell>
                <TableCell>{row.fkExemplar || '---'}</TableCell>
                <TableCell>{row.fkCliente || '---'}</TableCell>
                <TableCell>{row.fkFuncionario || '---'}</TableCell>
                <TableCell>
                  <div className="flex gap-2">
                    <button
                      onClick={() => handleEdit(row)}
                      className="text-blue-600 hover:text-blue-800 transition-colors"
                      title="Editar Empréstimo"
                    >
                      <Pencil size={12} />
                    </button>

                    <button
                      onClick={() => handleDelete(row.id)}
                      className="text-red-600 hover:text-red-800 transition-colors"
                      title="Excluir empréstimo"
                    >
                      <Trash2 size={8} />
                    </button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {/* Modal para editar data de devolução, sem shadcn mesmo */}
      {editingEmprestimo && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg space-y-4 w-80">
            <h2 className="text-lg font-semibold">Editar Data de Devolução</h2>
            <input
              type="datetime-local"
              className="w-full border rounded p-2"
              value={novaDataDevolucao}
              onChange={(e) => setNovaDataDevolucao(e.target.value)}
            />
            <div className="flex justify-end gap-2">
              <button
                onClick={() => setEditingEmprestimo(null)}
                className="px-4 py-2 bg-gray-200 hover:bg-gray-300 rounded"
              >
                Sair
              </button>
              <button
                onClick={handleSubmitEdit}
                className="px-4 py-2 bg-blue-600 text-white hover:bg-blue-700 rounded"
              >
                Salvar
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default EmprestimoTable;
