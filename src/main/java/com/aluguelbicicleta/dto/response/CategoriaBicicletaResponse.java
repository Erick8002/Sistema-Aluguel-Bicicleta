package com.aluguelbicicleta.dto.response;

import com.aluguelbicicleta.model.CategoriaBicicleta;

public record CategoriaBicicletaResponse(
        Long id,
        String nome,
        Double valorPorHora
) {

    public CategoriaBicicletaResponse(CategoriaBicicleta categoria) {
        this(
            categoria.getId(),
            categoria.getNome(),
            categoria.getValorPorHora()
        );
    }
}
