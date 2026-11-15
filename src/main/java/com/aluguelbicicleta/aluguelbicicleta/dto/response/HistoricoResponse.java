package com.aluguelbicicleta.aluguelbicicleta.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoricoResponse(
        Long id,
        String tipo,
        Long aluguelId,
        Long clienteId,
        Long bicicletaId,
        BigDecimal valor,
        LocalDateTime dataHora
) {
}
