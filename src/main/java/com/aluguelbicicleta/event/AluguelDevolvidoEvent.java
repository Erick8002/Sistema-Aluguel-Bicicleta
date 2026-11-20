package com.aluguelbicicleta.event;

import java.time.LocalDateTime;

public record AluguelDevolvidoEvent(
        Long aluguelId,
        Long clienteId,
        Long bicicletaId,
        Double valor,
        LocalDateTime ocorridoEm
) {
}
