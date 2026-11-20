package com.aluguelbicicleta.dto.response;

import com.aluguelbicicleta.model.Aluguel;

import java.time.LocalDateTime;

public record AluguelResponse(
        Long id,
        Long clienteId,
        Long bicicletaId,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Double valorHoraAplicado,
        Double valor
) {

    public AluguelResponse(Aluguel aluguel) {
        this(
                aluguel.getId(),
                aluguel.getCliente().getId(),
                aluguel.getBicicleta().getId(),
                aluguel.getDataInicio(),
                aluguel.getDataFim(),
                aluguel.getValorHoraAplicado(),
                aluguel.getValor()
        );
    }
}
