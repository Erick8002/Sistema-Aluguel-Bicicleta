package com.aluguelbicicleta.aluguelbicicleta.controller;

import com.aluguelbicicleta.aluguelbicicleta.dto.request.ClienteRequest;
import com.aluguelbicicleta.aluguelbicicleta.dto.response.ClienteResponse;
import com.aluguelbicicleta.aluguelbicicleta.model.Cliente;
import com.aluguelbicicleta.aluguelbicicleta.service.ClienteService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteResponse> listarClientes() {
        return clienteService.listarClientes()
                .stream()
                .map(ClienteResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscarPorId(@PathVariable Long id) {

        Cliente cliente = clienteService.buscarPorId(id);

        return new ClienteResponse(cliente);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse cadastrarCliente(
            @Valid @RequestBody ClienteRequest dados) {

        Cliente cliente = clienteService.cadastrarCliente(dados);

        return new ClienteResponse(cliente);
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest dadosAtualizados) {

        Cliente cliente = clienteService.atualizarCliente(id, dadosAtualizados);

        return new ClienteResponse(cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarCliente(@PathVariable Long id) {
        clienteService.deletarCliente(id);
    }
}