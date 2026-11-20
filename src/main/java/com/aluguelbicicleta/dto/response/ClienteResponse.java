package com.aluguelbicicleta.dto.response;

import com.aluguelbicicleta.model.Cliente;

public record ClienteResponse(
        Long id,
        String nome,
        String cpf,
        String telefone
) {

    public ClienteResponse(Cliente cliente) {
        this(
            cliente.getId(),
            cliente.getNome(),
            cliente.getCpf(),
            cliente.getTelefone()
        );
    }
}
