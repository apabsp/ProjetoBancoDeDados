import React, { useState } from 'react';
import EmprestimoForm from './EmprestimoForm';
import DeleteEmprestimoForm from './DeleteEmprestimoForm';
import VisualizarEmprestimos from './VisualizarEmprestimos';

export default function EmprestimoSection() {
  const [refreshKey, setRefreshKey] = useState(0);
  const bump = () => setRefreshKey(k => k + 1);

  return (
    <section className="space-y-6">
      <EmprestimoForm onSuccess={bump} />
      <DeleteEmprestimoForm onDelete={bump} />
      <VisualizarEmprestimos refreshKey={refreshKey} />
    </section>
  );
}
