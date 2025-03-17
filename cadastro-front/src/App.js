import React from 'react';
import ClienteForm from './components/ClienteForm';
import Header from './components/Header';

function App() {
  return (
    <div className="min-vh-100 bg-light">
      <Header />
      <main className="py-4">
        <ClienteForm />
      </main>
      <footer className="bg-light py-4 mt-4 border-top">
        <div className="container text-center text-muted">
          <p>&copy; {new Date().getFullYear()} John Doe - Sistema de Cadastro</p>
          <p className="small mt-1">Todos os direitos reservados</p>
        </div>
      </footer>
    </div>
  );
}

export default App;