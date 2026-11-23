package com.aluguelbicicleta.repository;

import com.aluguelbicicleta.model.Bicicleta;
import com.aluguelbicicleta.model.enums.StatusBicicleta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {

    boolean existsByCategoria_Id(Long categoriaId);

    List<Bicicleta> findByStatus(StatusBicicleta status, Pageable pageable);
}