package com.br.form.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClienteDTO(
        @NotBlank(message = "Nome é obrigatório") String nomeCompleto,
        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "^\\d{11}$", message = "CPF deve conter 11 dígitos numéricos") String cpf,
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido") String email,
        @NotBlank(message = "Cor preferida é obrigatória") String corPreferida,
        String observacoes
) {}