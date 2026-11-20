package com.aluguelbicicleta.service;

import com.aluguelbicicleta.event.AluguelDevolvidoEvent;
import com.aluguelbicicleta.event.AluguelIniciadoEvent;

import com.aluguelbicicleta.model.Aluguel;
import com.aluguelbicicleta.model.Bicicleta;
import com.aluguelbicicleta.model.Cliente;
import com.aluguelbicicleta.model.Historico;
import com.aluguelbicicleta.model.enums.TipoHistorico;

import com.aluguelbicicleta.repository.AluguelRepository;
import com.aluguelbicicleta.repository.BicicletaRepository;
import com.aluguelbicicleta.repository.ClienteRepository;
import com.aluguelbicicleta.repository.HIstoricoRepository;

import com.aluguelbicicleta.exception.NotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoricoService {

    private final HIstoricoRepository historicoRepository;
    private final AluguelRepository aluguelRepository;
    private final ClienteRepository clienteRepository;
    private final BicicletaRepository bicicletaRepository;

    public HistoricoService(
            HIstoricoRepository historicoRepository,
            AluguelRepository aluguelRepository,
            ClienteRepository clienteRepository,
            BicicletaRepository bicicletaRepository) {
        this.historicoRepository = historicoRepository;
        this.aluguelRepository = aluguelRepository;
        this.clienteRepository = clienteRepository;
        this.bicicletaRepository = bicicletaRepository;
    }

    public List<Historico> listarHistorico() {
        return historicoRepository.findAllByOrderByIdAsc();
    }

    public Historico buscarPorId(Long id) {
        return historicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Histórico não encontrado!"
                ));
    }

    @Transactional
    public void registrarInicioAluguel(AluguelIniciadoEvent evento) {
        Aluguel aluguel = aluguelRepository.getReferenceById(evento.aluguelId());
        Cliente cliente = clienteRepository.getReferenceById(evento.clienteId());
        Bicicleta bicicleta = bicicletaRepository.getReferenceById(evento.bicicletaId());

        Historico historico = new Historico(
                TipoHistorico.ALUGUEL,
                aluguel,
                cliente,
                bicicleta,
                null,
                evento.ocorridoEm()
        );

        historicoRepository.save(historico);
    }

    @Transactional
    public void registrarDevolucao(AluguelDevolvidoEvent evento) {
        Aluguel aluguel = aluguelRepository.getReferenceById(evento.aluguelId());
        Cliente cliente = clienteRepository.getReferenceById(evento.clienteId());
        Bicicleta bicicleta = bicicletaRepository.getReferenceById(evento.bicicletaId());

        Historico historico = new Historico(
                TipoHistorico.DEVOLUCAO,
                aluguel,
                cliente,
                bicicleta,
                evento.valor(),
                evento.ocorridoEm()
        );

        historicoRepository.save(historico);
    }
}
