package com.lojas.moria.repository;

import com.lojas.moria.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long > {

    boolean existsByTelefone(String telefone);
}
