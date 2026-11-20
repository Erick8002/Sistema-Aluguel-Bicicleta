package com.aluguelbicicleta.dto.request;

import jakarta.validation.constraints.*;

public record CategoriaBicicletaRequest(

        @NotBlank(message = "O nome da categoria é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String nome,

        @NotNull(message = "O valor por hora é obrigatório.")
        @DecimalMin(value = "0.01", message = "O valor por hora deve ser maior que zero.")
        Double valorPorHora
) {
}
