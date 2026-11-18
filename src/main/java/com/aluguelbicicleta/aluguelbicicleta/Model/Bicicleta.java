package com.aluguelbicicleta.aluguelbicicleta.model;

import com.aluguelbicicleta.aluguelbicicleta.Model.enums.StatusBicicleta;
import jakarta.persistence.*;

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

    public Bicicleta() {
    }

    public Long getId() {
        return id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public StatusBicicleta getStatus() {
        return status;
    }

    public void setStatus(StatusBicicleta status) {
        this.status = status;
    }
}
