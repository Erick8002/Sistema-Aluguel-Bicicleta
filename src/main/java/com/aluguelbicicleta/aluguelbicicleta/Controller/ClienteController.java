package com.aluguelbicicleta.aluguelbicicleta.controller;

import com.aluguelbicicleta.aluguelbicicleta.Model.ClienteModel;
import com.aluguelbicicleta.aluguelbicicleta.Repository.ClienteRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public List<ClienteModel> listar() {
        return clienteRepository.findAllByOrderByIdAsc();
    }
}