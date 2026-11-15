package com.aluguelbicicleta.aluguelbicicleta.dto.response;

import com.aluguelbicicleta.aluguelbicicleta.Model.enums.StatusBicicleta;

public record BicicletaResponse(
        Long id,
        String modelo,
        StatusBicicleta status
) {
}
