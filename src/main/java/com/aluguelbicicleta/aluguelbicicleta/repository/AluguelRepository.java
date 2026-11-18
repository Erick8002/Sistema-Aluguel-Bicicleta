package com.aluguelbicicleta.aluguelbicicleta.repository;

import com.aluguelbicicleta.aluguelbicicleta.model.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    // Lista todos os aluguéis ordenados pelo ID de forma ascendente
    List<Aluguel> findAllByOrderByIdAsc();
}
