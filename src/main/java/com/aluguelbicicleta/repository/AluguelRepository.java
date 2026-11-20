package com.aluguelbicicleta.repository;

import com.aluguelbicicleta.model.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {

    List<Aluguel> findAllByOrderByIdAsc();
}
