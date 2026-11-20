package com.aluguelbicicleta.model;

import com.aluguelbicicleta.model.enums.TipoHistorico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "historico")
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 20, nullable = false)
    private TipoHistorico tipo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_aluguel", nullable = false)
    private Aluguel aluguel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_bicicleta", nullable = false)
    private Bicicleta bicicleta;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    public Historico(
            TipoHistorico tipo,
            Aluguel aluguel,
            Cliente cliente,
            Bicicleta bicicleta,
            Double valor,
            LocalDateTime dataHora) {
        this.tipo = tipo;
        this.aluguel = aluguel;
        this.cliente = cliente;
        this.bicicleta = bicicleta;
        this.valor = valor;
        this.dataHora = dataHora;
    }
}
