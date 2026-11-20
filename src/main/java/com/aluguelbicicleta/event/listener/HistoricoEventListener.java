package com.aluguelbicicleta.event.listener;

import com.aluguelbicicleta.event.AluguelDevolvidoEvent;
import com.aluguelbicicleta.event.AluguelIniciadoEvent;
import com.aluguelbicicleta.service.HistoricoService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class HistoricoEventListener {

    private final HistoricoService historicoService;

    public HistoricoEventListener(HistoricoService historicoService) {
        this.historicoService = historicoService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void aoIniciarAluguel(AluguelIniciadoEvent evento) {
        historicoService.registrarInicioAluguel(evento);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void aoRegistrarDevolucao(AluguelDevolvidoEvent evento) {
        historicoService.registrarDevolucao(evento);
    }
}
