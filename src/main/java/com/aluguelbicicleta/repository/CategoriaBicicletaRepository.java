package com.aluguelbicicleta.repository;

import com.aluguelbicicleta.model.CategoriaBicicleta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaBicicletaRepository extends JpaRepository<CategoriaBicicleta, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
