package com.lojas.moria.service;

import com.lojas.moria.dto.InicioResumoResponse;
import com.lojas.moria.dto.VendaResponse;
import com.lojas.moria.model.*;
import com.lojas.moria.repository.MovimentacaoFinanceiraRepository;
import com.lojas.moria.repository.ProdutoRepository;
import com.lojas.moria.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InicioService {

    private final VendaRepository vendaRepository;
    private final MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
    private final ProdutoRepository produtoRepository;
    private final VendaService vendaService;

    public InicioService(
            VendaRepository vendaRepository,
            MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository,
            ProdutoRepository produtoRepository,
            VendaService vendaService) {

        this.vendaRepository = vendaRepository;
        this.movimentacaoFinanceiraRepository = movimentacaoFinanceiraRepository;
        this.produtoRepository = produtoRepository;
        this.vendaService = vendaService;
    }

    @Transactional(readOnly = true)
    public InicioResumoResponse buscarResumo() {

        InicioResumoResponse resumo = new InicioResumoResponse();

        // VENDAS DE HOJE
        LocalDate hoje = LocalDate.now();

        LocalDateTime inicioHoje = hoje.atStartOfDay();
        LocalDateTime fimHoje = hoje.plusDays(1).atStartOfDay();

        List<Venda> vendasHoje =
                vendaRepository.findByDataGreaterThanEqualAndDataLessThan(
                        inicioHoje,
                        fimHoje
                );

        BigDecimal totalVendas = vendasHoje.stream()
                .map(Venda::getTotal)
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        InicioResumoResponse.VendasHoje vendas =
                new InicioResumoResponse.VendasHoje();

        vendas.setTotal(totalVendas);
        vendas.setQuantidade((long) vendasHoje.size());

        resumo.setVendasHoje(vendas);

        // CAIXA DO MÊS
        YearMonth mesAtual = YearMonth.now();

        LocalDateTime inicioMes =
                mesAtual.atDay(1).atStartOfDay();

        LocalDateTime fimMes =
                mesAtual.plusMonths(1).atDay(1).atStartOfDay();

        BigDecimal entradas =
                movimentacaoFinanceiraRepository.somarPorTipoStatusEPeriodo(
                        TipoMovimentacaoFinanceira.ENTRADA,
                        StatusMovimentacaoFinanceira.PAGO,
                        inicioMes,
                        fimMes
                );

        BigDecimal saidas =
                movimentacaoFinanceiraRepository.somarPorTipoStatusEPeriodo(
                        TipoMovimentacaoFinanceira.SAIDA,
                        StatusMovimentacaoFinanceira.PAGO,
                        inicioMes,
                        fimMes
                );

        InicioResumoResponse.CaixaMes caixa =
                new InicioResumoResponse.CaixaMes();

        caixa.setEntradas(entradas != null ? entradas : BigDecimal.ZERO);
        caixa.setSaidas(saidas != null ? saidas : BigDecimal.ZERO);

        resumo.setCaixaMes(caixa);

        // ESTOQUE BAIXO
        List<InicioResumoResponse.ProdutoEstoqueBaixo> estoqueBaixo =
                produtoRepository.buscarProdutosComEstoqueBaixo()
                        .stream()
                        .map(produto -> {
                            InicioResumoResponse.ProdutoEstoqueBaixo item =
                                    new InicioResumoResponse.ProdutoEstoqueBaixo();

                            item.setCodigo(produto.getCodigo());
                            item.setNome(produto.getNome());
                            item.setQuantidade(
                                    produto.getQuantidadeEmEstoque()
                            );

                            return item;
                        })
                        .toList();

        resumo.setEstoqueBaixo(estoqueBaixo);

        // ÚLTIMAS CINCO VENDAS
        DateTimeFormatter formatoHora =
                DateTimeFormatter.ofPattern("HH:mm");

        List<InicioResumoResponse.UltimaVenda> ultimasVendas =
                vendaService.listarVendas(5)
                        .stream()
                        .map(venda -> {
                            InicioResumoResponse.UltimaVenda item =
                                    new InicioResumoResponse.UltimaVenda();

                            item.setId(venda.getId());
                            item.setCliente(venda.getClienteNome());

                            if (venda.getData() != null) {
                                item.setHora(
                                        venda.getData()
                                                .format(formatoHora)
                                );
                            }

                            item.setPagamento(
                                    venda.getPagamento() != null
                                            ? venda.getPagamento().name()
                                            : ""
                            );

                            item.setTotal(venda.getTotal());

                            return item;
                        })
                        .toList();

        resumo.setUltimasVendas(ultimasVendas);

        return resumo;
    }
}