package com.br.form.service.impl;

import com.br.form.exception.ResourceNotFoundException;
import com.br.form.model.Cliente;
import com.br.form.model.dto.ClienteDTO;
import com.br.form.repository.ClienteRepository;
import com.br.form.service.ClienteService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public Cliente cadastrarCliente(ClienteDTO clienteDTO) {
        if(clienteRepository.existsByCpf(clienteDTO.cpf())){
            throw new DataIntegrityViolationException("CPF já cadastrado.");
        }
        if(clienteRepository.existsByEmail(clienteDTO.email())){
            throw new DataIntegrityViolationException("Email já cadastrado.");
        }

        Cliente cliente = new Cliente(
                clienteDTO.nomeCompleto(),
                clienteDTO.cpf(),
                clienteDTO.email(),
                clienteDTO.corPreferida(),
                clienteDTO.observacoes()
        );

        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPeloId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o id: " +  id));
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o CPF: " + cpf));
    }
}
