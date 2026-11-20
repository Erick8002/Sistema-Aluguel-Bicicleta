package com.aluguelbicicleta.repository;

import com.aluguelbicicleta.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HIstoricoRepository extends JpaRepository<Historico, Long> {

    List<Historico> findAllByOrderByIdAsc();
}
