package com.lojas.moria.repository;

import com.lojas.moria.model.Venda;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findAllByOrderByDataDesc();

    List<Venda> findAllByOrderByDataDesc(Pageable pageable);
}