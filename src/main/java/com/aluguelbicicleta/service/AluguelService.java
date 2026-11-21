package com.aluguelbicicleta.service;

import com.aluguelbicicleta.dto.request.AluguelRequest;
import com.aluguelbicicleta.event.AluguelDevolvidoEvent;
import com.aluguelbicicleta.event.AluguelIniciadoEvent;
import com.aluguelbicicleta.exception.RestricaoException;
import com.aluguelbicicleta.exception.NotFoundException;

import com.aluguelbicicleta.model.Aluguel;
import com.aluguelbicicleta.model.Bicicleta;
import com.aluguelbicicleta.model.Cliente;
import com.aluguelbicicleta.model.enums.StatusBicicleta;

import com.aluguelbicicleta.repository.AluguelRepository;
import com.aluguelbicicleta.repository.BicicletaRepository;
import com.aluguelbicicleta.repository.ClienteRepository;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.List;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final ClienteRepository clienteRepository;
    private final BicicletaRepository bicicletaRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AluguelService(
            AluguelRepository aluguelRepository,
            ClienteRepository clienteRepository,
            BicicletaRepository bicicletaRepository,
            ApplicationEventPublisher eventPublisher) {
        this.aluguelRepository = aluguelRepository;
        this.clienteRepository = clienteRepository;
        this.bicicletaRepository = bicicletaRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<Aluguel> listarAlugueis() {
        return aluguelRepository.findAllByOrderByIdAsc();
    }

    public Aluguel buscarPorId(Long id) {
        return aluguelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Aluguel não encontrado!"
                ));
    }

    @Transactional
    public Aluguel registrarAluguel(AluguelRequest dados) {
        Cliente cliente = clienteRepository.findById(dados.clienteId())
                .orElseThrow(() -> new NotFoundException(
                        "Cliente não encontrado!"
                ));

        Bicicleta bicicleta = bicicletaRepository.findById(dados.bicicletaId())
                .orElseThrow(() -> new NotFoundException(
                        "Bicicleta não encontrada!"
                ));

        if (bicicleta.getStatus() != StatusBicicleta.DISPONIVEL) {
            throw new RestricaoException(
                    "A bicicleta não está disponível para aluguel."
            );
        }

        Aluguel aluguel = new Aluguel(
                cliente,
                bicicleta,
                LocalDateTime.now(),
                bicicleta.getCategoria().getValorPorHora()
        );

        bicicleta.setStatus(StatusBicicleta.ALUGADA);
        bicicletaRepository.save(bicicleta);

        Aluguel aluguelSalvo = aluguelRepository.save(aluguel);
        eventPublisher.publishEvent(new AluguelIniciadoEvent(
                aluguelSalvo.getId(),
                cliente.getId(),
                bicicleta.getId(),
                aluguelSalvo.getDataInicio()
        ));

        return aluguelSalvo;
    }

    @Transactional
    public Aluguel registrarDevolucao(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Aluguel não encontrado!"
                ));

        if (aluguel.getDataFim() != null) {
            throw new RestricaoException(
                    "Este aluguel já foi finalizado."
            );
        }

        LocalDateTime dataFim = LocalDateTime.now();

        long minutos = Math.max(1, (ChronoUnit.SECONDS.between(aluguel.getDataInicio(), dataFim) + 59) / 60);
        double valor = Math.round(minutos * aluguel.getValorHoraAplicado() / 60.0 * 100.0) / 100.0;

        aluguel.setDataFim(dataFim);
        aluguel.setValor(valor);

        Bicicleta bicicleta = aluguel.getBicicleta();
        bicicleta.setStatus(StatusBicicleta.DISPONIVEL);
        bicicletaRepository.save(bicicleta);

        Aluguel aluguelAtualizado = aluguelRepository.save(aluguel);
        eventPublisher.publishEvent(new AluguelDevolvidoEvent(
                aluguelAtualizado.getId(),
                aluguelAtualizado.getCliente().getId(),
                bicicleta.getId(),
                aluguelAtualizado.getValor(),
                aluguelAtualizado.getDataFim()
        ));

        return aluguelAtualizado;
    }
}
