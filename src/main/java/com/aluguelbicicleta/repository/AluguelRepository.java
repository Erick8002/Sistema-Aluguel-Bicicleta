package com.aluguelbicicleta.repository;

import com.aluguelbicicleta.model.Aluguel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {

    List<Aluguel> findByDataFimIsNull(Pageable pageable);

    List<Aluguel> findByCliente_Id(Long clienteId, Pageable pageable);

    List<Aluguel> findByCliente_IdAndDataFimIsNull(Long clienteId, Pageable pageable);
}