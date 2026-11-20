package com.aluguelbicicleta.model;

import com.aluguelbicicleta.model.enums.StatusBicicleta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "bicicleta")
public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusBicicleta status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaBicicleta categoria;

    public Bicicleta(String modelo, StatusBicicleta status, CategoriaBicicleta categoria) {
        this.modelo = modelo;
        this.status = status;
        this.categoria = categoria;
    }
}
