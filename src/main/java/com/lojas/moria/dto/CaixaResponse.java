package com.lojas.moria.dto;

import com.lojas.moria.model.TipoMovimentacaoFinanceira;
import com.lojas.moria.model.StatusMovimentacaoFinanceira;
import com.lojas.moria.model.OrigemMovimentacaoFinanceira;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CaixaResponse {

    private Long id;
    private TipoMovimentacaoFinanceira tipo;
    private String descricao;
    private String categoria;
    private BigDecimal valor;
    private String forma;
    private String pessoa;
    private String observacao;
    private StatusMovimentacaoFinanceira status;
    private OrigemMovimentacaoFinanceira origem;
    private LocalDate vencimento;
    private LocalDateTime data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoMovimentacaoFinanceira getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacaoFinanceira tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getPessoa() {
        return pessoa;
    }

    public void setPessoa(String pessoa) {
        this.pessoa = pessoa;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public StatusMovimentacaoFinanceira getStatus() {
        return status;
    }

    public void setStatus(StatusMovimentacaoFinanceira status) {
        this.status = status;
    }

    public OrigemMovimentacaoFinanceira getOrigem() {
        return origem;
    }

    public void setOrigem(OrigemMovimentacaoFinanceira origem) {
        this.origem = origem;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}