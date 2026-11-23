package com.aluguelbicicleta.dto.request;

import com.aluguelbicicleta.model.enums.StatusBicicleta;
import jakarta.validation.constraints.NotNull;

public record StatusBicicletaRequest(

        @NotNull(message = "O status é obrigatório.")
        StatusBicicleta status
) {
}