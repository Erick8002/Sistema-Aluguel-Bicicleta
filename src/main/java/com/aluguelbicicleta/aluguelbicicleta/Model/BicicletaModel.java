package com.aluguelbicicleta.aluguelbicicleta.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "bicicleta")
public class BicicletaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Column(name = "disponivel", nullable = false)
    private Boolean disponivel;

    public BicicletaModel() {
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

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
}
