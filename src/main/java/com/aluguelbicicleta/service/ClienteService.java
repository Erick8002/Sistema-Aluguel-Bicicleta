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

        String cpf = somenteDigitos(dados.cpf());

        if (clienteRepository.existsByCpf(cpf)) {
            throw new RestricaoException(
                    "O CPF informado já está cadastrado."
            );
        }

        Cliente cliente = new Cliente(
                dados.nome(),
                cpf,
                dados.telefone()
        );

        return clienteRepository.save(cliente);
    }

    public Cliente atualizarCliente(Long id, ClienteRequest dados) {

        Cliente cliente = buscarPorId(id);

        String cpf = somenteDigitos(dados.cpf());

        if (clienteRepository.existsByCpfAndIdNot(cpf, id)) {
            throw new RestricaoException(
                    "O CPF informado já pertence a outro cliente."
            );
        }

        cliente.setNome(dados.nome());
        cliente.setCpf(cpf);
        cliente.setTelefone(dados.telefone());

        return clienteRepository.save(cliente);
    }

    private String somenteDigitos(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    public void deletarCliente(Long id) {
        Cliente cliente = buscarPorId(id);

        clienteRepository.delete(cliente);
    }
}
