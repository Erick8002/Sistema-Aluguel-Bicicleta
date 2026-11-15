package com.aluguelbicicleta.aluguelbicicleta.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres.")
        String nome,

        @NotBlank(message = "O CPF é obrigatório.")
        @Pattern(
                regexp = "\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
                message = "O CPF deve conter 11 dígitos, com ou sem pontuação."
        )
        String cpf,

        @NotBlank(message = "O telefone é obrigatório.")
        @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres.")
        String telefone
) {
}
