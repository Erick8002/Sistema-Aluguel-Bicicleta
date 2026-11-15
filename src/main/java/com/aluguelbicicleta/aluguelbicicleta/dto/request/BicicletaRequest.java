package com.aluguelbicicleta.aluguelbicicleta.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BicicletaRequest(
        @NotBlank(message = "O modelo é obrigatório.")
        @Size(max = 255, message = "O modelo deve ter no máximo 255 caracteres.")
        String modelo
) {
}
