package com.aluguelbicicleta.controller;

import com.aluguelbicicleta.dto.request.CategoriaBicicletaRequest;
import com.aluguelbicicleta.dto.response.CategoriaBicicletaResponse;
import com.aluguelbicicleta.service.CategoriaBicicletaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias-bicicleta")
public class CategoriaBicicletaController {

    private final CategoriaBicicletaService categoriaBicicletaService;

    public CategoriaBicicletaController(CategoriaBicicletaService categoriaBicicletaService) {
        this.categoriaBicicletaService = categoriaBicicletaService;
    }

    @GetMapping
    public List<CategoriaBicicletaResponse> listarCategorias() {
        return categoriaBicicletaService.listarCategorias()
                .stream()
                .map(CategoriaBicicletaResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoriaBicicletaResponse buscarPorId(@PathVariable Long id) {

        return new CategoriaBicicletaResponse(categoriaBicicletaService.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaBicicletaResponse cadastrarCategoria(
            @Valid @RequestBody CategoriaBicicletaRequest dados) {

        return new CategoriaBicicletaResponse(categoriaBicicletaService.cadastrarCategoria(dados));
    }

    @PutMapping("/{id}")
    public CategoriaBicicletaResponse atualizarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaBicicletaRequest dados) {

        return new CategoriaBicicletaResponse(categoriaBicicletaService.atualizarCategoria(id, dados));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarCategoria(@PathVariable Long id) {

        categoriaBicicletaService.deletarCategoria(id);
    }
}
