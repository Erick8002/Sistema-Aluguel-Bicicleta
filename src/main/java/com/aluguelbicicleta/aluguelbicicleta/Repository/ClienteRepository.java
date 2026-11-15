package com.aluguelbicicleta.aluguelbicicleta.Repository;

import com.aluguelbicicleta.aluguelbicicleta.Model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {
    List<ClienteModel> findAllByOrderByIdAsc();

    // verificar se o CPF já existe ao cadastrar
    Optional<ClienteModel> findByCpf(String cpf);
}