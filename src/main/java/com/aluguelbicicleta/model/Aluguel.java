package com.aluguelbicicleta.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "aluguel")
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_bicicleta", nullable = false)
    private Bicicleta bicicleta;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @Column(name = "valor_hora_aplicado", nullable = false)
    private Double valorHoraAplicado;

    @Column(name = "valor")
    private Double valor;

    public Aluguel(
            Cliente cliente,
            Bicicleta bicicleta,
            LocalDateTime dataInicio,
            Double valorHoraAplicado) {
        this.cliente = cliente;
        this.bicicleta = bicicleta;
        this.dataInicio = dataInicio;
        this.valorHoraAplicado = valorHoraAplicado;
    }
}
