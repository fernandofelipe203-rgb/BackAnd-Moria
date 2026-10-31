package com.lojas.moria.controller;

import com.lojas.moria.dto.ProdutoEstoqueResponse;
import com.lojas.moria.dto.ProdutoVendaResponse;
import com.lojas.moria.model.CategoriaProduto;
import com.lojas.moria.model.Produto;
import com.lojas.moria.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;


    //Buscando produtos
    @GetMapping
    public List<ProdutoVendaResponse> listar() {
        return produtoService.listarProdutos()
                .stream()
                .map(this::converterParaVendaResponse)
                .toList();
    }

    @GetMapping("/estoque")
    public List<ProdutoEstoqueResponse> listarParaEstoque() {
        return produtoService.listarProdutos()
                .stream()
                .map(produto -> {
                    ProdutoEstoqueResponse response = new ProdutoEstoqueResponse();

                    response.setId(produto.getId());
                    response.setCodigo(produto.getCodigo());
                    response.setNome(produto.getNome());
                    response.setPreco(produto.getPreco());
                    response.setQuantidadeEmEstoque(
                            produto.getQuantidadeEmEstoque()
                    );
                    response.setEstoqueMinimo(produto.getEstoqueMinimo());
                    response.setUnidade(produto.getUnidade());

                    return response;
                })
                .toList();
    }


    //Buscar produto por ID
    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }
    //buscar por nome
    @GetMapping("/buscar")
    public List<Produto> buscarPorNome(@RequestParam String nome) {
        return produtoService.buscarPorNome(nome);
    }

    @PostMapping
    public Produto cadastrar(@Valid @RequestBody Produto produto) {
        System.out.println("Nome: " + produto.getNome());
        System.out.println("Categoria: " + produto.getCategoria());
        System.out.println("Preço: " + produto.getPreco());
        System.out.println("Quantidade: " + produto.getQuantidadeEmEstoque());

        return produtoService.cadastrarProduto(produto);
    }
    @PutMapping("/{id}")
    public Produto atulizar(@PathVariable Long id, @RequestBody Produto produto){
        return produtoService.atualizarProduto(id,produto);
    }

    @DeleteMapping("/{id}")
    public  String excluir(@PathVariable Long id){
       produtoService.excluirProduto(id);
       return "Produto excluido com sucesso";
    }
    @GetMapping("/categoria/{categoria}")
    public List<Produto> buscarPorCategoria(
            @PathVariable CategoriaProduto categoria) {
        return produtoService.buscarPorCategoria(categoria);
    }
    private ProdutoVendaResponse converterParaVendaResponse(Produto produto) {
        ProdutoVendaResponse response = new ProdutoVendaResponse();

        response.setId(produto.getId());
        response.setCodigo(produto.getCodigo());
        response.setNome(produto.getNome());
        response.setPreco(produto.getPreco());
        response.setEstoque(produto.getQuantidadeEmEstoque());

        return response;
    }
}


