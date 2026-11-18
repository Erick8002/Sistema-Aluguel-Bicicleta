package com.aluguelbicicleta.aluguelbicicleta.repository;

import com.aluguelbicicleta.aluguelbicicleta.model.Bicicleta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {
    // Retorna uma lista apenas com as bikes que estão com o status disponível
    List<Bicicleta> findByStatus(String status);
}