package com.aluguelbicicleta.repository;

import com.aluguelbicicleta.model.Bicicleta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {

    boolean existsByCategoria_Id(Long categoriaId);
}