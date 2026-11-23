package com.aluguelbicicleta.service;

import com.aluguelbicicleta.dto.request.BicicletaRequest;

import com.aluguelbicicleta.exception.NotFoundException;

import com.aluguelbicicleta.model.Bicicleta;
import com.aluguelbicicleta.model.CategoriaBicicleta;
import com.aluguelbicicleta.model.enums.StatusBicicleta;

import com.aluguelbicicleta.repository.BicicletaRepository;
import com.aluguelbicicleta.repository.CategoriaBicicletaRepository;

import com.aluguelbicicleta.exception.RestricaoException;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    public List<Bicicleta> listarBicicletas(StatusBicicleta status, Integer page, Integer size) {

        Pageable pageable = criarPaginacao(page, size);

        if (status != null) {
            return bicicletaRepository.findByStatus(status, pageable);
        }

        return bicicletaRepository.findAll(pageable).getContent();
    }

    public Bicicleta buscarPorId(Long id) {
        return bicicletaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Bicicleta não encontrada!"
                ));
    }

    private Pageable criarPaginacao(Integer page, Integer size) {
        Sort ordenacao = Sort.by("id");

        if (page == null || size == null) {
            return Pageable.unpaged(ordenacao);
        }

        return PageRequest.of(Math.max(page, 0), Math.max(size, 1), ordenacao);
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

    public Bicicleta alterarStatus(Long id, StatusBicicleta novoStatus) {

        Bicicleta bicicleta = buscarPorId(id);

        if (bicicleta.getStatus() == StatusBicicleta.ALUGADA) {
            throw new RestricaoException(
                    "A bicicleta está alugada. Registre a devolução antes de alterar o status."
            );
        }

        if (novoStatus == StatusBicicleta.ALUGADA) {
            throw new RestricaoException(
                    "O status ALUGADA só pode ser definido ao registrar um aluguel."
            );
        }

        bicicleta.setStatus(novoStatus);

        return bicicletaRepository.save(bicicleta);
    }
}
