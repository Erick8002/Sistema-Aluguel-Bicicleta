package com.aluguelbicicleta.controller;

import com.aluguelbicicleta.dto.response.HistoricoResponse;
import com.aluguelbicicleta.service.HistoricoService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historicos")
public class HistoricoController {

    private final HistoricoService historicoService;

    public HistoricoController(HistoricoService historicoService) {
        this.historicoService = historicoService;
    }

    @GetMapping
    public List<HistoricoResponse> listarHistorico() {
        return historicoService.listarHistorico()
                .stream()
                .map(HistoricoResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public HistoricoResponse buscarPorId(@PathVariable Long id) {

        return new HistoricoResponse(historicoService.buscarPorId(id));
    }
}
