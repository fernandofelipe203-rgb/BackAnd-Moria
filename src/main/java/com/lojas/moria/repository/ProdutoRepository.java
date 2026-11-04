package com.lojas.moria.repository;

import com.lojas.moria.model.CategoriaProduto;
import com.lojas.moria.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    List<Produto> findByCategoria(CategoriaProduto categoria);

    List<Produto> findByQuantidadeEmEstoqueLessThanEqualOrderByQuantidadeEmEstoqueAsc(
            Integer quantidade
    );

    @Query("""
    SELECT p
    FROM Produto p
    WHERE p.quantidadeEmEstoque <= p.estoqueMinimo
    ORDER BY p.quantidadeEmEstoque ASC
""")
    List<Produto> buscarProdutosComEstoqueBaixo();
}
