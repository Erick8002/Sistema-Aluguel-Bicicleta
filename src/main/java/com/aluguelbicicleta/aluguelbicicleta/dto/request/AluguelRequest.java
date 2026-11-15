package com.aluguelbicicleta.aluguelbicicleta.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AluguelRequest(
        @NotNull(message = "O ID do cliente é obrigatório.")
        @Positive(message = "O ID do cliente deve ser positivo.")
        Long clienteId,

        @NotNull(message = "O ID da bicicleta é obrigatório.")
        @Positive(message = "O ID da bicicleta deve ser positivo.")
        Long bicicletaId
) {
}
