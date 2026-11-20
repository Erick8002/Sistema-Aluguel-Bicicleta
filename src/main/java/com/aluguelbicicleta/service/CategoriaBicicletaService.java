package com.aluguelbicicleta.service;

import com.aluguelbicicleta.dto.request.CategoriaBicicletaRequest;

import com.aluguelbicicleta.exception.RestricaoException;
import com.aluguelbicicleta.exception.NotFoundException;

import com.aluguelbicicleta.model.CategoriaBicicleta;

import com.aluguelbicicleta.repository.BicicletaRepository;
import com.aluguelbicicleta.repository.CategoriaBicicletaRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaBicicletaService {

    private final CategoriaBicicletaRepository categoriaBicicletaRepository;
    private final BicicletaRepository bicicletaRepository;

    public CategoriaBicicletaService(
            CategoriaBicicletaRepository categoriaBicicletaRepository,
            BicicletaRepository bicicletaRepository) {
        this.categoriaBicicletaRepository = categoriaBicicletaRepository;
        this.bicicletaRepository = bicicletaRepository;
    }

    public List<CategoriaBicicleta> listarCategorias() {
        return categoriaBicicletaRepository.findAll();
    }

    public CategoriaBicicleta buscarPorId(Long id) {
        return categoriaBicicletaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Categoria de bicicleta não encontrada!"
                ));
    }

    public CategoriaBicicleta cadastrarCategoria(CategoriaBicicletaRequest dados) {
        if (categoriaBicicletaRepository.existsByNomeIgnoreCase(dados.nome())) {
            throw new RestricaoException(
                    "Já existe uma categoria com esse nome."
            );
        }

        CategoriaBicicleta categoria = new CategoriaBicicleta(
                dados.nome(),
                dados.valorPorHora()
        );

        return categoriaBicicletaRepository.save(categoria);
    }

    public CategoriaBicicleta atualizarCategoria(Long id, CategoriaBicicletaRequest dados) {
        CategoriaBicicleta categoria = buscarPorId(id);

        if (categoriaBicicletaRepository.existsByNomeIgnoreCaseAndIdNot(
                dados.nome(),
                id)) {
            throw new RestricaoException(
                    "Já existe outra categoria com esse nome."
            );
        }

        categoria.setNome(dados.nome());
        categoria.setValorPorHora(dados.valorPorHora());

        return categoriaBicicletaRepository.save(categoria);
    }

    public void deletarCategoria(Long id) {
        CategoriaBicicleta categoria = buscarPorId(id);

        if (bicicletaRepository.existsByCategoria_Id(id)) {
            throw new RestricaoException(
                    "Não é possível excluir uma categoria que possui bicicletas."
            );
        }

        categoriaBicicletaRepository.delete(categoria);
    }
}
