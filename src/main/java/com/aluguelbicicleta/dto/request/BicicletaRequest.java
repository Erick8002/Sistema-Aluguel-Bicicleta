package com.aluguelbicicleta.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record BicicletaRequest(

        @NotBlank(message = "O modelo é obrigatório.")
        @Size(max = 255, message = "O modelo deve ter no máximo 255 caracteres.")
        String modelo,

        @NotNull(message = "O ID da categoria é obrigatório.")
        @Positive(message = "O ID da categoria deve ser positivo.")
        Long categoriaId
) {
}
