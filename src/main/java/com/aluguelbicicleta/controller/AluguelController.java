package com.aluguelbicicleta.controller;

import com.aluguelbicicleta.dto.request.AluguelRequest;
import com.aluguelbicicleta.dto.response.AluguelResponse;
import com.aluguelbicicleta.service.AluguelService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @GetMapping
    public List<AluguelResponse> listarAlugueis(
            @RequestParam(required = false) Boolean emAberto,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        return aluguelService.listarAlugueis(emAberto, clienteId, page, size)
                .stream()
                .map(AluguelResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public AluguelResponse buscarPorId(@PathVariable Long id) {

        return new AluguelResponse(aluguelService.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AluguelResponse registrarAluguel(
            @Valid @RequestBody AluguelRequest dados) {

        return new AluguelResponse(aluguelService.registrarAluguel(dados));
    }

    @PostMapping("/{id}/devolucao")
    public AluguelResponse registrarDevolucao(@PathVariable Long id) {

        return new AluguelResponse(aluguelService.registrarDevolucao(id));
    }
}
