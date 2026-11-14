package com.aluguelbicicleta.aluguelbicicleta.Repository;

import com.aluguelbicicleta.aluguelbicicleta.Model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {
    List<ClienteModel> findAllByOrderByIdAsc();
}