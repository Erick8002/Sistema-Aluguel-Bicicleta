package com.aluguelbicicleta.aluguelbicicleta.Repository;

import com.aluguelbicicleta.aluguelbicicleta.Model.AluguelModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AluguelRepository extends JpaRepository<AluguelModel, Long> {
    // Lista todos os aluguéis ordenados pelo ID de forma ascendente
    List<AluguelModel> findAllByOrderByIdAsc();
}
