package com.aluguelbicicleta.aluguelbicicleta.Repository;

import com.aluguelbicicleta.aluguelbicicleta.Model.BicicletaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BicicletaRepository extends JpaRepository<BicicletaModel, Long> {
    // Retorna uma lista apenas com as bikes que estão com o status disponível
    List<BicicletaModel> findByStatus(String status);
}