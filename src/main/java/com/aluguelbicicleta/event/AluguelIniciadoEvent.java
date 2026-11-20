package com.aluguelbicicleta.event;

import java.time.LocalDateTime;

public record AluguelIniciadoEvent(
        Long aluguelId,
        Long clienteId,
        Long bicicletaId,
        LocalDateTime ocorridoEm
) {
}
