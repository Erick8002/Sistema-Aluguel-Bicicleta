package com.aluguelbicicleta.aluguelbicicleta.dto.response;

import java.time.LocalDateTime;

public record AluguelResponse(
        Long id,
        Long clienteId,
        Long bicicletaId,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Double valor
) {
}
