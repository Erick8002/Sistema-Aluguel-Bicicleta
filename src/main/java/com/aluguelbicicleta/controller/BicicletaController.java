package com.aluguelbicicleta.controller;

import com.aluguelbicicleta.dto.request.BicicletaRequest;
import com.aluguelbicicleta.dto.response.BicicletaResponse;
import com.aluguelbicicleta.service.BicicletaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bicicletas")
public class BicicletaController {

    private final BicicletaService bicicletaService;

    public BicicletaController(BicicletaService bicicletaService) {
        this.bicicletaService = bicicletaService;
    }

    @GetMapping
    public List<BicicletaResponse> listarBicicletas() {
        return bicicletaService.listarBicicletas()
                .stream()
                .map(BicicletaResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public BicicletaResponse buscarPorId(@PathVariable Long id) {

        return new BicicletaResponse(bicicletaService.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BicicletaResponse cadastrarBicicleta(
            @Valid @RequestBody BicicletaRequest dados) {

        return new BicicletaResponse(bicicletaService.cadastrarBicicleta(dados));
    }

    @PutMapping("/{id}")
    public BicicletaResponse atualizarBicicleta(
            @PathVariable Long id,
            @Valid @RequestBody BicicletaRequest dadosAtualizados) {

        return new BicicletaResponse(bicicletaService.atualizarBicicleta(id, dadosAtualizados));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarBicicleta(@PathVariable Long id) {

        bicicletaService.deletarBicicleta(id);
    }
}
