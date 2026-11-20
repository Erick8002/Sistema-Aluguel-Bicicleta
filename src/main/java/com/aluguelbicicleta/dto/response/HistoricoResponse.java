package com.aluguelbicicleta.dto.response;

import com.aluguelbicicleta.model.Historico;
import com.aluguelbicicleta.model.enums.TipoHistorico;

import java.time.LocalDateTime;

public record HistoricoResponse(
        Long id,
        TipoHistorico tipo,
        Long aluguelId,
        Long clienteId,
        Long bicicletaId,
        Double valor,
        LocalDateTime dataHora
) {

    public HistoricoResponse(Historico historico) {
        this(
                historico.getId(),
                historico.getTipo(),
                historico.getAluguel().getId(),
                historico.getCliente().getId(),
                historico.getBicicleta().getId(),
                historico.getValor(),
                historico.getDataHora()
        );
    }
}
