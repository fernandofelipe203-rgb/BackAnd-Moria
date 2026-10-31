package com.lojas.moria.service;

import com.lojas.moria.execption.ProdutoComMovimentacoesException;
import com.lojas.moria.execption.ProdutoNaoEncontradoException;
import com.lojas.moria.model.CategoriaProduto;
import com.lojas.moria.model.Produto;
import com.lojas.moria.repository.ItemMovimentacaoEstoqueRepository;
import com.lojas.moria.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
/*Aqui e onde eu coloco as regras de nogocios, como eu quero que o sitema se comporte. O meu service esta conversando com o
repository. e o repository*/
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ItemMovimentacaoEstoqueRepository itemMovimentacaoEstoqueRepository;


    public List<Produto> listarProdutos(){
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id){
        return produtoRepository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException("Produto nao econtrado com o ID: " + id));
    }
    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Produto cadastrarProduto(Produto produto){
        return produtoRepository.save(produto);
    }
    public Produto atualizarProduto(Long id, Produto produto) {

        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(
                        "Produto não encontrado com o ID: " + id
                ));

        produtoExistente.setNome(produto.getNome());
        produtoExistente.setPreco(produto.getPreco());
        produtoExistente.setDescricao(produto.getDescricao());
        produtoExistente.setCategoria(produto.getCategoria());
        produtoExistente.setCodigo(produto.getCodigo());
        produtoExistente.setEstoqueMinimo(produto.getEstoqueMinimo());
        produtoExistente.setUnidade(produto.getUnidade());

        // Preservamos a quantidade atual do estoque.
        // O estoque será alterado somente pelas movimentações.

        return produtoRepository.save(produtoExistente);
    }


    public void excluirProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(
                        "Produto não encontrado com o ID: " + id
                ));

        boolean possuiMovimentacoes =
                itemMovimentacaoEstoqueRepository.existsByProduto_Id(id);

        if (possuiMovimentacoes) {
            throw new ProdutoComMovimentacoesException(
                    "Não é possível excluir este produto porque ele possui movimentações de estoque registradas."
            );
        }

        produtoRepository.delete(produto);
    }

    public List<Produto> buscarPorCategoria(CategoriaProduto categoria) {
        return produtoRepository.findByCategoria(categoria);
    }
}
