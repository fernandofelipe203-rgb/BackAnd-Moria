package com.lojas.moria.dto;

import java.math.BigDecimal;
import java.util.List;

public class InicioResumoResponse {

    private VendasHoje vendasHoje;
    private CaixaMes caixaMes;
    private List<ProdutoEstoqueBaixo> estoqueBaixo;
    private List<UltimaVenda> ultimasVendas;

    public VendasHoje getVendasHoje() {
        return vendasHoje;
    }

    public void setVendasHoje(VendasHoje vendasHoje) {
        this.vendasHoje = vendasHoje;
    }

    public CaixaMes getCaixaMes() {
        return caixaMes;
    }

    public void setCaixaMes(CaixaMes caixaMes) {
        this.caixaMes = caixaMes;
    }

    public List<ProdutoEstoqueBaixo> getEstoqueBaixo() {
        return estoqueBaixo;
    }

    public void setEstoqueBaixo(List<ProdutoEstoqueBaixo> estoqueBaixo) {
        this.estoqueBaixo = estoqueBaixo;
    }

    public List<UltimaVenda> getUltimasVendas() {
        return ultimasVendas;
    }

    public void setUltimasVendas(List<UltimaVenda> ultimasVendas) {
        this.ultimasVendas = ultimasVendas;
    }

    public static class VendasHoje {
        private BigDecimal total;
        private Long quantidade;

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public Long getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Long quantidade) {
            this.quantidade = quantidade;
        }
    }

    public static class CaixaMes {
        private BigDecimal entradas;
        private BigDecimal saidas;

        public BigDecimal getEntradas() {
            return entradas;
        }

        public void setEntradas(BigDecimal entradas) {
            this.entradas = entradas;
        }

        public BigDecimal getSaidas() {
            return saidas;
        }

        public void setSaidas(BigDecimal saidas) {
            this.saidas = saidas;
        }
    }

    public static class ProdutoEstoqueBaixo {
        private String codigo;
        private String nome;
        private Integer quantidade;

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public Integer getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
        }
    }

    public static class UltimaVenda {
        private Long id;
        private String cliente;
        private String hora;
        private String pagamento;
        private BigDecimal total;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCliente() {
            return cliente;
        }

        public void setCliente(String cliente) {
            this.cliente = cliente;
        }

        public String getHora() {
            return hora;
        }

        public void setHora(String hora) {
            this.hora = hora;
        }

        public String getPagamento() {
            return pagamento;
        }

        public void setPagamento(String pagamento) {
            this.pagamento = pagamento;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }
    }
}