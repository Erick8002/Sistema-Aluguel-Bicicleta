package com.aluguelbicicleta.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "categoria_bicicleta")
public class CategoriaBicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 100, nullable = false, unique = true)
    private String nome;

    @Column(name = "valor_por_hora", nullable = false)
    private Double valorPorHora;

    public CategoriaBicicleta(String nome, Double valorPorHora) {
        this.nome = nome;
        this.valorPorHora = valorPorHora;
    }

}
