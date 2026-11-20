package com.aluguelbicicleta.dto.response;

import com.aluguelbicicleta.model.Bicicleta;
import com.aluguelbicicleta.model.enums.StatusBicicleta;

public record BicicletaResponse(
        Long id,
        String modelo,
        StatusBicicleta status,
        Long categoriaId,
        String categoriaNome
) {

    public BicicletaResponse(Bicicleta bicicleta) {
        this(
            bicicleta.getId(),
            bicicleta.getModelo(),
            bicicleta.getStatus(),
            bicicleta.getCategoria().getId(),
            bicicleta.getCategoria().getNome()
        );
    }
}
