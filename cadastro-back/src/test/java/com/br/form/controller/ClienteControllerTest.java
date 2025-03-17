package com.br.form.controller;


import com.br.form.model.Cliente;
import com.br.form.model.dto.ClienteDTO;
import com.br.form.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ClienteControllerTest {

    MockMvc mockMvc;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private Cliente clienteMock;
    private ClienteDTO clienteDTOMock;
    private List<Cliente> clientesMock;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();

        clienteMock = new Cliente();
        clienteMock.setId(1L);
        clienteMock.setNome("João Silva");
        clienteMock.setCpf("12345678901");
        clienteMock.setEmail("joao@example.com");

        clienteDTOMock = new ClienteDTO("João Silva", "12345678901",
                "joao@example.com", "Vermelho", "Teste Unitário");

        Cliente clienteMock2 = new Cliente("Maria Souza", "98765432109",
                "maria@email.com", "Vermelho", "Teste Unitario");
        clienteMock2.setId(2L);  // Definir o ID explicitamente

        clientesMock = Arrays.asList(clienteMock, clienteMock2);

    }

    @Test
    public void testCadastrarCliente() throws Exception {
        when(clienteService.cadastrarCliente(any(ClienteDTO.class))).thenReturn(clienteMock);

        String clienteJson = convertToJson(clienteDTOMock);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clienteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sucesso").value(true))
                .andExpect(jsonPath("$.mensagem").value("Cliente cadastrado com sucesso!"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("João Silva"))
                .andExpect(jsonPath("$.data.cpf").value("12345678901"));
    }

    @Test
    public void testListarClientes() throws Exception {
        when(clienteService.listarTodos()).thenReturn(clientesMock);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Maria Souza"));
    }

    @Test
    public void testBuscarPorId() throws Exception {
        when(clienteService.buscarPeloId(anyLong())).thenReturn(clienteMock);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    public void testBuscarPorCpf() throws Exception {
        when(clienteService.buscarPorCpf(anyString())).thenReturn(clienteMock);

        mockMvc.perform(get("/api/clientes/cpf/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    public void testCadastrarClienteComDadosInvalidos() throws Exception {
        ClienteDTO clienteInvalido = new ClienteDTO("User Teste", "12345678901", "emailexample.com", "Rosa", "Teste");

        String clienteJson = convertToJson(clienteInvalido);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clienteJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBuscarPorIdInexistente() throws Exception {
        when(clienteService.buscarPeloId(anyLong()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        mockMvc.perform(get("/api/clientes/999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testBuscarPorCpfInexistente() throws Exception {
        when(clienteService.buscarPorCpf(anyString()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        mockMvc.perform(get("/api/clientes/cpf/99999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCadastrarClienteComCpfDuplicado() throws Exception {
        when(clienteService.cadastrarCliente(any(ClienteDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado"));

        String clienteJson = convertToJson(clienteDTOMock);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clienteJson))
                .andExpect(status().isConflict());
    }

    private String convertToJson(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}

