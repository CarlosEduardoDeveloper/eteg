package com.br.form.service;

import com.br.form.model.Cliente;
import com.br.form.model.dto.ClienteDTO;

import java.util.List;

public interface ClienteService {
    Cliente cadastrarCliente(ClienteDTO clienteDTO);
    List<Cliente> listarTodos();
    Cliente buscarPeloId(Long id);
    Cliente buscarPorCpf(String cpf);
}
