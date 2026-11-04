
package com.lojas.moria.service;
import org.springframework.transaction.annotation.Transactional;
import com.lojas.moria.dto.ItemVendaRequest;
import com.lojas.moria.dto.ItemVendaResponse;
import com.lojas.moria.dto.VendaRequest;
import com.lojas.moria.dto.VendaResponse;
import com.lojas.moria.model.*;
import com.lojas.moria.repository.ClienteRepository;
import com.lojas.moria.repository.MovimentacaoFinanceiraRepository;
import com.lojas.moria.repository.ProdutoRepository;
import com.lojas.moria.repository.VendaRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendaService {
    private final ClienteRepository clienteRepository;
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueService movimentacaoEstoqueService;
    private final MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

    public VendaService(
            VendaRepository vendaRepository,
            ProdutoRepository produtoRepository,
            ClienteRepository clienteRepository,
            MovimentacaoEstoqueService movimentacaoEstoqueService,
            MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository) {

        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
        this.movimentacaoFinanceiraRepository = movimentacaoFinanceiraRepository;
    }

    @Transactional
    public VendaResponse registrarVenda(VendaRequest request) {

        if (request == null
                || request.getPagamento() == null
                || request.getItens() == null
                || request.getItens().isEmpty()) {
            throw new IllegalArgumentException(
                    "Informe o pagamento e pelo menos um produto");
        }

        Venda venda = new Venda();

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cliente não encontrado: " + request.getClienteId()));

        venda.setCliente(cliente);

        venda.setPagamento(request.getPagamento());

        Integer parcelas = request.getParcelas() == null
                ? 1
                : request.getParcelas();

        if (request.getPagamento() == FormaPagamento.CARTAO) {
            if (parcelas < 1 || parcelas > 12) {
                throw new IllegalArgumentException(
                        "O cartão permite parcelamento de 1 a 12 vezes");
            }
        } else {
            parcelas = 1;
        }

        venda.setParcelas(parcelas);
        venda.setTotal(BigDecimal.ZERO);

        BigDecimal total = BigDecimal.ZERO;

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTipo(TipoMovimentacao.SAIDA);
        movimentacao.setDescricao("Saída referente a uma venda");

        for (ItemVendaRequest itemRequest : request.getItens()) {

            if (itemRequest == null
                    || itemRequest.getProdutoId() == null
                    || itemRequest.getQuantidade() == null
                    || itemRequest.getQuantidade() <= 0) {
                throw new IllegalArgumentException(
                        "Informe um produto válido e uma quantidade maior que zero");
            }

            Produto produto = produtoRepository
                    .findById(itemRequest.getProdutoId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Produto não encontrado: "
                                    + itemRequest.getProdutoId()));

            if (produto.getPreco() == null
                    || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalStateException(
                        "O produto possui um preço inválido: "
                                + produto.getNome());
            }

            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setProduto(produto);
            itemVenda.setQuantidade(itemRequest.getQuantidade());

            // O preço vem do banco, não do frontend.
            itemVenda.setPrecoUnitario(produto.getPreco());

            venda.adicionarItem(itemVenda);

            BigDecimal subtotal = produto.getPreco().multiply(
                    BigDecimal.valueOf(itemRequest.getQuantidade()));

            total = total.add(subtotal);

            ItemMovimentacaoEstoque itemMovimentacao =
                    new ItemMovimentacaoEstoque();

            itemMovimentacao.setProduto(produto);
            itemMovimentacao.setQuantidade(itemRequest.getQuantidade());

            movimentacao.adicionarItem(itemMovimentacao);
        }

        venda.setTotal(total);

        // Salva a venda e seus itens.
        Venda vendaSalva = vendaRepository.save(venda);

        // Registra a saída e atualiza o estoque usando a regra existente.
        movimentacao.setDescricao(
                "Saída referente à venda #" + vendaSalva.getId());

        movimentacaoEstoqueService.registrarMovimentacao(movimentacao);

        // Registra a entrada financeira referente à venda.
        MovimentacaoFinanceira entrada = new MovimentacaoFinanceira();

        entrada.setTipo(TipoMovimentacaoFinanceira.ENTRADA);
        entrada.setDescricao("Recebimento da venda #" + vendaSalva.getId());
        entrada.setValor(vendaSalva.getTotal());
        entrada.setFormaPagamento(vendaSalva.getPagamento());
        entrada.setForma(vendaSalva.getPagamento().name());

        entrada.setCategoria("Venda");
        entrada.setPessoa(vendaSalva.getCliente().getNome());
        entrada.setStatus(StatusMovimentacaoFinanceira.PAGO);
        entrada.setOrigem(OrigemMovimentacaoFinanceira.VENDA);

        entrada.setVenda(vendaSalva);

        movimentacaoFinanceiraRepository.save(entrada);

        return converterParaResponse(vendaSalva);
    }
    @jakarta.transaction.Transactional

    public List<VendaResponse> listarVendas(int quantidade) {

        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero");
        }

        Pageable pagina = PageRequest.of(0, quantidade);

        return vendaRepository.findAllByOrderByDataDesc(pagina)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    private VendaResponse converterParaResponse(Venda venda) {
        VendaResponse response = new VendaResponse();

        response.setId(venda.getId());
        response.setData(venda.getData());
        response.setClienteId(venda.getCliente().getId());
        response.setClienteNome(venda.getCliente().getNome());
        response.setPagamento(venda.getPagamento());
        response.setParcelas(venda.getParcelas());
        response.setTotal(venda.getTotal());

        List<ItemVendaResponse> itens = venda.getItens()
                .stream()
                .map(item -> {
                    ItemVendaResponse itemResponse = new ItemVendaResponse();

                    itemResponse.setProdutoId(item.getProduto().getId());
                    itemResponse.setCodigo(item.getProduto().getCodigo());
                    itemResponse.setNome(item.getProduto().getNome());
                    itemResponse.setQuantidade(item.getQuantidade());
                    itemResponse.setPrecoUnitario(item.getPrecoUnitario());
                    itemResponse.setSubtotal(item.getSubtotal());

                    return itemResponse;
                })
                .collect(Collectors.toList());

        response.setItens(itens);

        return response;
    }
}
