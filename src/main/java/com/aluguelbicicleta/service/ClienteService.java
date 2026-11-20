package com.aluguelbicicleta.service;

import com.aluguelbicicleta.dto.request.ClienteRequest;

import com.aluguelbicicleta.exception.RestricaoException;
import com.aluguelbicicleta.exception.NotFoundException;

import com.aluguelbicicleta.model.Cliente;

import com.aluguelbicicleta.repository.ClienteRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(
                    "Cliente não encontrado!"
            ));
    }

    public Cliente cadastrarCliente(ClienteRequest dados) {

        if (clienteRepository.existsByCpf(dados.cpf())) {
            throw new RestricaoException(
                    "O CPF informado já está cadastrado."
            );
        }

        Cliente cliente = new Cliente(
                dados.nome(),
                dados.cpf(),
                dados.telefone()
        );

        return clienteRepository.save(cliente);
    }

    public Cliente atualizarCliente(Long id, ClienteRequest dados) {

        Cliente cliente = buscarPorId(id);

        if (clienteRepository.existsByCpfAndIdNot(dados.cpf(), id)) {
            throw new RestricaoException(
                    "O CPF informado já pertence a outro cliente."
            );
        }

        cliente.setNome(dados.nome());
        cliente.setCpf(dados.cpf());
        cliente.setTelefone(dados.telefone());

        return clienteRepository.save(cliente);
    }

    public void deletarCliente(Long id) {
        Cliente cliente = buscarPorId(id);

        clienteRepository.delete(cliente);
    }
}
