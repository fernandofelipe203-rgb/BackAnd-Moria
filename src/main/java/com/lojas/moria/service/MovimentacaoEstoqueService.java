
package com.lojas.moria.service;

import com.lojas.moria.execption.EstoqueInsuficienteException;
import com.lojas.moria.execption.MovimentacaoNaoEncontradaException;
import com.lojas.moria.execption.ProdutoNaoEncontradoException;
import com.lojas.moria.model.ItemMovimentacaoEstoque;
import com.lojas.moria.model.MovimentacaoEstoque;
import com.lojas.moria.model.Produto;
import com.lojas.moria.model.TipoMovimentacao;
import com.lojas.moria.repository.MovimentacaoEstoqueRepository;
import com.lojas.moria.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    private final ProdutoRepository produtoRepository;


    public MovimentacaoEstoqueService(
            MovimentacaoEstoqueRepository movimentacaoEstoqueRepository,
            ProdutoRepository produtoRepository) {

        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
        this.produtoRepository = produtoRepository;
    }



    public MovimentacaoEstoque salvar(MovimentacaoEstoque movimentacao) {
        return movimentacaoEstoqueRepository.save(movimentacao);
    }
    public Produto buscarProduto(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() ->
                        new ProdutoNaoEncontradoException("Produto não encontrado"));
    }


    @Transactional
    public void registrarMovimentacao(MovimentacaoEstoque movimentacao) {

        if (movimentacao.getTipo() == null) {
            throw new IllegalArgumentException(
                    "O tipo da movimentação é obrigatório");
        }

        if (movimentacao.getTipo() != TipoMovimentacao.ENTRADA
                && movimentacao.getTipo() != TipoMovimentacao.SAIDA) {
            throw new IllegalArgumentException(
                    "Tipo de movimentação não suportado");
        }

        if (movimentacao.getItens() == null
                || movimentacao.getItens().isEmpty()) {
            throw new IllegalArgumentException(
                    "A movimentação deve possuir pelo menos um produto");
        }

        // Primeiro, validamos todos os itens sem alterar o estoque.
        java.util.Map<Long, Integer> quantidadesPorProduto =
                new java.util.HashMap<>();

        java.util.Map<Long, Produto> produtos =
                new java.util.HashMap<>();

        for (ItemMovimentacaoEstoque item : movimentacao.getItens()) {

            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new IllegalArgumentException(
                        "A quantidade deve ser maior que zero");
            }

            if (item.getProduto() == null
                    || item.getProduto().getId() == null) {
                throw new IllegalArgumentException(
                        "O produto do item é obrigatório");
            }

            Long produtoId = item.getProduto().getId();

            Produto produto = produtos.get(produtoId);

            if (produto == null) {
                produto = buscarProduto(produtoId);
                produtos.put(produtoId, produto);
            }

            if (produto.getQuantidadeEmEstoque() == null
                    || produto.getQuantidadeEmEstoque() < 0) {
                throw new IllegalStateException(
                        "O estoque atual do produto é inválido: "
                                + produto.getNome());
            }

            item.setProduto(produto);

            int quantidadeAcumulada = quantidadesPorProduto.getOrDefault(
                    produtoId, 0);

            if (item.getQuantidade() > Integer.MAX_VALUE - quantidadeAcumulada) {
                throw new IllegalArgumentException(
                        "A quantidade total ultrapassa o limite permitido");
            }

            quantidadesPorProduto.put(
                    produtoId, quantidadeAcumulada + item.getQuantidade());
        }

        // Depois, validamos o total de cada produto.
        for (java.util.Map.Entry<Long, Integer> entrada
                : quantidadesPorProduto.entrySet()) {

            Produto produto = produtos.get(entrada.getKey());
            int quantidadeTotal = entrada.getValue();
            int estoqueAtual = produto.getQuantidadeEmEstoque();

            if (movimentacao.getTipo() == TipoMovimentacao.SAIDA
                    && quantidadeTotal > estoqueAtual) {
                throw new EstoqueInsuficienteException(
                        "Estoque insuficiente para o produto: "
                                + produto.getNome());
            }

            if (movimentacao.getTipo() == TipoMovimentacao.ENTRADA
                    && quantidadeTotal > Integer.MAX_VALUE - estoqueAtual) {
                throw new IllegalArgumentException(
                        "A entrada ultrapassa o limite permitido para o produto: "
                                + produto.getNome());
            }
        }

        // Só alteramos o estoque depois de todas as validações passarem.
        for (java.util.Map.Entry<Long, Integer> entrada
                : quantidadesPorProduto.entrySet()) {

            Produto produto = produtos.get(entrada.getKey());
            int quantidadeTotal = entrada.getValue();
            int estoqueAtual = produto.getQuantidadeEmEstoque();

            if (movimentacao.getTipo() == TipoMovimentacao.ENTRADA) {
                produto.setQuantidadeEmEstoque(estoqueAtual + quantidadeTotal);
            } else {
                produto.setQuantidadeEmEstoque(estoqueAtual - quantidadeTotal);
            }
        }

        movimentacaoEstoqueRepository.save(movimentacao);
    }




    public List<MovimentacaoEstoque> listarTodas() {
        return movimentacaoEstoqueRepository.findAll();
    }


    public MovimentacaoEstoque buscarPorId(Integer id) {
        return movimentacaoEstoqueRepository.findById(id)
                .orElseThrow(() ->
                        new MovimentacaoNaoEncontradaException(
                                "Movimentação não encontrada"));
    }



}
