
package com.lojas.moria.repository;

import com.lojas.moria.model.ItemMovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemMovimentacaoEstoqueRepository
        extends JpaRepository<ItemMovimentacaoEstoque, Integer> {

    boolean existsByProduto_Id(Long produtoId);
}
