package com.lojas.moria.repository;

import com.lojas.moria.model.CategoriaProduto;
import com.lojas.moria.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    List<Produto> findByCategoria(CategoriaProduto categoria);
}
