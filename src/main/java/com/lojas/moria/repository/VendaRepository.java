package com.lojas.moria.repository;

import com.lojas.moria.model.Venda;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findAllByOrderByDataDesc();

    List<Venda> findAllByOrderByDataDesc(Pageable pageable);

    boolean existsByClienteId(Long clienteId);


    List<Venda> findByDataGreaterThanEqualAndDataLessThan(
            LocalDateTime inicio,
            LocalDateTime fim
    );
}