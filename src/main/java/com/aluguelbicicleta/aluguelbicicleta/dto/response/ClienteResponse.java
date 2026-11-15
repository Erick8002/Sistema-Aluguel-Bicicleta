package com.aluguelbicicleta.aluguelbicicleta.dto.response;

public record ClienteResponse(
        Long id,
        String nome,
        String cpf,
        String telefone
) {
}
