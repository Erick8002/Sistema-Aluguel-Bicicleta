package com.aluguelbicicleta.service;

import com.aluguelbicicleta.dto.request.BicicletaRequest;

import com.aluguelbicicleta.exception.NotFoundException;

import com.aluguelbicicleta.model.Bicicleta;
import com.aluguelbicicleta.model.CategoriaBicicleta;
import com.aluguelbicicleta.model.enums.StatusBicicleta;

import com.aluguelbicicleta.repository.BicicletaRepository;
import com.aluguelbicicleta.repository.CategoriaBicicletaRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BicicletaService {

    private final BicicletaRepository bicicletaRepository;
    private final CategoriaBicicletaRepository categoriaBicicletaRepository;

    public BicicletaService(
            BicicletaRepository bicicletaRepository,
            CategoriaBicicletaRepository categoriaBicicletaRepository) {
        this.bicicletaRepository = bicicletaRepository;
        this.categoriaBicicletaRepository = categoriaBicicletaRepository;
    }

    public List<Bicicleta> listarBicicletas() {
        return bicicletaRepository.findAll();
    }

    public Bicicleta buscarPorId(Long id) {
        return bicicletaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Bicicleta não encontrada!"
                ));
    }

    private CategoriaBicicleta buscarCategoria(Long categoriaId) {
        return categoriaBicicletaRepository.findById(categoriaId)
                .orElseThrow(() -> new NotFoundException(
                        "Categoria de bicicleta não encontrada!"
                ));
    }

    public Bicicleta cadastrarBicicleta(BicicletaRequest dados) {
        CategoriaBicicleta categoria = buscarCategoria(dados.categoriaId());

        Bicicleta bicicleta = new Bicicleta(
                dados.modelo(),
                StatusBicicleta.DISPONIVEL,
                categoria
        );

        return bicicletaRepository.save(bicicleta);
    }

    public Bicicleta atualizarBicicleta(Long id, BicicletaRequest dados) {
        CategoriaBicicleta categoria = buscarCategoria(dados.categoriaId());

        Bicicleta bicicleta = buscarPorId(id);

        bicicleta.setModelo(dados.modelo());
        bicicleta.setCategoria(categoria);

        return bicicletaRepository.save(bicicleta);
    }

    public void deletarBicicleta(Long id) {

        Bicicleta bicicleta = buscarPorId(id);

        bicicletaRepository.delete(bicicleta);
    }
}
