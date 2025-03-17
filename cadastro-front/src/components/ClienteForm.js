import React, { useState } from 'react';
import { Button, Card, Col, Container, Form, Row } from 'react-bootstrap';
import api from '../services/api';
import Feedback from './Feedback';

const ClienteForm = () => {
  const coresArcoIris = [
    'Vermelho', 'Laranja', 'Amarelo', 'Verde', 'Azul', 'Anil', 'Violeta'
  ];

  const [formData, setFormData] = useState({
    nomeCompleto: '',
    cpf: '',
    email: '',
    corPreferida: '',
    observacoes: '',
  });

  const [errors, setErrors] = useState({});
  const [feedback, setFeedback] = useState({ show: false, success: false, message: '' });
  const [loading, setLoading] = useState(false);

  const validateForm = () => {
    const newErrors = {};

    if (!formData.nomeCompleto.trim()) {
      newErrors.nomeCompleto = 'Nome completo é obrigatório';
    }

    if (!formData.cpf.trim()) {
      newErrors.cpf = 'CPF é obrigatório';
    } else if (!/^\d{11}$/.test(formData.cpf)) {
      newErrors.cpf = 'CPF deve conter 11 dígitos numéricos';
    }

    if (!formData.email.trim()) {
      newErrors.email = 'E-mail é obrigatório';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'E-mail inválido';
    }

    if (!formData.corPreferida) {
      newErrors.corPreferida = 'Cor preferida é obrigatória';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const formatCpf = (value) => {
    // Remove caracteres não numéricos
    const cpfNumerico = value.replace(/\D/g, '');
    return cpfNumerico;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === 'cpf') {
      setFormData({ ...formData, [name]: formatCpf(value) });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      const response = await api.post('/clientes', formData);

      setFeedback({
        show: true,
        success: true,
        message: 'Cadastro realizado com sucesso!'
      });

      // Limpa o formulário após sucesso
      setFormData({
        nomeCompleto: '',
        cpf: '',
        email: '',
        corPreferida: '',
        observacoes: '',
      });

    } catch (error) {
      let errorMessage = 'Erro ao realizar cadastro. Tente novamente.';

      if (error.response) {
        if (error.response.data.erros) {
          errorMessage = Object.values(error.response.data.erros).join(', ');
        } else if (error.response.data.mensagem) {
          errorMessage = error.response.data.mensagem;
        }
      }

      setFeedback({
        show: true,
        success: false,
        message: errorMessage
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container>
      <Row className="justify-content-center">
        <Col md={8} lg={6}>
          <Card className="shadow-sm">
            <Card.Body className="p-4">
              <h2 className="text-center mb-4">Cadastre-se</h2>

              {feedback.show && (
                <Feedback
                  success={feedback.success}
                  message={feedback.message}
                  onClose={() => setFeedback({ ...feedback, show: false })}
                />
              )}


              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label>Nome Completo *</Form.Label>
                  <Form.Control
                    type="text"
                    name="nomeCompleto"
                    value={formData.nomeCompleto}
                    onChange={handleChange}
                    isInvalid={!!errors.nomeCompleto}
                    placeholder="Seu nome completo"
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.nomeCompleto}
                  </Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>CPF * (apenas números)</Form.Label>
                  <Form.Control
                    type="text"
                    name="cpf"
                    value={formData.cpf}
                    onChange={handleChange}
                    maxLength={11}
                    isInvalid={!!errors.cpf}
                    placeholder="Seu CPF (apenas números)"
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.cpf}
                  </Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>E-mail *</Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    isInvalid={!!errors.email}
                    placeholder="Seu e-mail"
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.email}
                  </Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Cor Preferida *</Form.Label>
                  <Form.Select
                    name="corPreferida"
                    value={formData.corPreferida}
                    onChange={handleChange}
                    isInvalid={!!errors.corPreferida}
                  >
                    <option value="">Selecione uma cor</option>
                    {coresArcoIris.map((cor) => (
                      <option key={cor} value={cor}>
                        {cor}
                      </option>
                    ))}
                  </Form.Select>
                  <Form.Control.Feedback type="invalid">
                    {errors.corPreferida}
                  </Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-4">
                  <Form.Label>Observações</Form.Label>
                  <Form.Control
                    as="textarea"
                    name="observacoes"
                    value={formData.observacoes}
                    onChange={handleChange}
                    rows={4}
                    placeholder="Observações adicionais (opcional)"
                  />
                </Form.Group>

                <div className="d-grid gap-2">
                  <Button
                    variant="primary"
                    type="submit"
                    disabled={loading}
                    className="py-2"
                  >
                    {loading ? 'Enviando...' : 'Enviar Cadastro'}
                  </Button>
                </div>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default ClienteForm;