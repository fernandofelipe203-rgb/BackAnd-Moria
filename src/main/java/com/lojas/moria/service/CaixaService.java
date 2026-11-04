package com.lojas.moria.service;

import com.lojas.moria.dto.CaixaRequest;
import com.lojas.moria.dto.CaixaResponse;
import com.lojas.moria.model.*;
import com.lojas.moria.repository.MovimentacaoFinanceiraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CaixaService {

    private final MovimentacaoFinanceiraRepository repository;

    public CaixaService(MovimentacaoFinanceiraRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CaixaResponse> listarLancamentos() {
        return repository.findAllByOrderByDataHoraDesc()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    @Transactional
    public CaixaResponse criarLancamento(CaixaRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Informe os dados do lançamento");
        }

        MovimentacaoFinanceira movimentacao = new MovimentacaoFinanceira();

        movimentacao.setTipo(request.getTipo());
        movimentacao.setDescricao(request.getDescricao());
        movimentacao.setCategoria(request.getCategoria());
        movimentacao.setValor(request.getValor());
        movimentacao.setPessoa(request.getPessoa());
        movimentacao.setObservacao(request.getObservacao());
        movimentacao.setForma(request.getForma());
        movimentacao.setVencimento(request.getVencimento());

        movimentacao.setOrigem(OrigemMovimentacaoFinanceira.MANUAL);

        if (request.getVencimento() != null) {
            movimentacao.setStatus(StatusMovimentacaoFinanceira.PENDENTE);
        } else {
            movimentacao.setStatus(StatusMovimentacaoFinanceira.PAGO);
            movimentacao.setDataPagamento(LocalDateTime.now());
        }

        MovimentacaoFinanceira salva = repository.save(movimentacao);

        return converterParaResponse(salva);
    }

    @Transactional
    public CaixaResponse baixarLancamento(Long id) {

        MovimentacaoFinanceira movimentacao = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Lançamento não encontrado: " + id));

        if (movimentacao.getOrigem() != OrigemMovimentacaoFinanceira.MANUAL) {
            throw new IllegalArgumentException(
                    "Não é permitido alterar uma movimentação automática de venda");
        }

        if (movimentacao.getStatus() == StatusMovimentacaoFinanceira.PAGO) {
            throw new IllegalArgumentException(
                    "Este lançamento já está pago");
        }

        movimentacao.setStatus(StatusMovimentacaoFinanceira.PAGO);
        movimentacao.setDataPagamento(LocalDateTime.now());

        MovimentacaoFinanceira salva = repository.save(movimentacao);

        return converterParaResponse(salva);
    }
    @Transactional
    public CaixaResponse atualizarLancamento(
            Long id,
            com.lojas.moria.dto.CaixaAtualizacaoRequest request) {

        MovimentacaoFinanceira movimentacao = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Lançamento não encontrado: " + id));

        if (movimentacao.getOrigem() != OrigemMovimentacaoFinanceira.MANUAL) {
            throw new IllegalArgumentException(
                    "Não é permitido editar uma movimentação automática de venda");
        }

        if (request.getDescricao() != null) {
            movimentacao.setDescricao(request.getDescricao());
        }

        if (request.getCategoria() != null) {
            movimentacao.setCategoria(request.getCategoria());
        }

        if (request.getValor() != null) {
            if (request.getValor().signum() <= 0) {
                throw new IllegalArgumentException(
                        "O valor deve ser maior que zero");
            }
            movimentacao.setValor(request.getValor());
        }

        if (request.getForma() != null) {
            movimentacao.setForma(request.getForma());
        }

        if (request.getPessoa() != null) {
            movimentacao.setPessoa(request.getPessoa());
        }

        if (request.getObservacao() != null) {
            movimentacao.setObservacao(request.getObservacao());
        }

        movimentacao.setVencimento(request.getVencimento());

        return converterParaResponse(repository.save(movimentacao));
    }
    @Transactional
    public void removerLancamento(Long id) {

        MovimentacaoFinanceira movimentacao = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Lançamento não encontrado: " + id));

        if (movimentacao.getOrigem() != OrigemMovimentacaoFinanceira.MANUAL) {
            throw new IllegalArgumentException(
                    "Não é permitido excluir uma movimentação automática de venda");
        }

        repository.delete(movimentacao);
    }

    private CaixaResponse converterParaResponse(
            MovimentacaoFinanceira movimentacao) {

        CaixaResponse response = new CaixaResponse();

        response.setId(movimentacao.getId());
        response.setTipo(movimentacao.getTipo());
        response.setDescricao(movimentacao.getDescricao());
        response.setCategoria(movimentacao.getCategoria());
        response.setValor(movimentacao.getValor());

        response.setForma(
                movimentacao.getForma() != null
                        ? movimentacao.getForma()
                        : movimentacao.getFormaPagamento() != null
                        ? movimentacao.getFormaPagamento().name()
                        : null
        );

        response.setPessoa(movimentacao.getPessoa());
        response.setObservacao(movimentacao.getObservacao());
        response.setStatus(movimentacao.getStatus());
        response.setOrigem(movimentacao.getOrigem());
        response.setVencimento(movimentacao.getVencimento());
        response.setData(
                movimentacao.getStatus() == StatusMovimentacaoFinanceira.PAGO
                        && movimentacao.getDataPagamento() != null
                        ? movimentacao.getDataPagamento()
                        : movimentacao.getDataHora()
        );

        return response;
    }
}