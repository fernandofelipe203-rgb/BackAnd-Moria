    package com.lojas.moria.model;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotNull;
    import jakarta.validation.constraints.Positive;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "movimentacoes_financeiras")
    public class MovimentacaoFinanceira {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull(message = "O tipo da movimentação é obrigatório")
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TipoMovimentacaoFinanceira tipo;

        @Column(nullable = false)
        private String descricao;

        @Column(length = 30)
        private String forma;

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        @Column(nullable = false, precision = 12, scale = 2)
        private BigDecimal valor;

        @Column(nullable = false)
        private LocalDateTime dataHora;

        @Enumerated(EnumType.STRING)
        @Column(nullable = true)
        private FormaPagamento formaPagamento;

        private String categoria;

        private String pessoa;

        @Column(length = 1000)
        private String observacao;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private StatusMovimentacaoFinanceira status = StatusMovimentacaoFinanceira.PAGO;

        private LocalDate vencimento;
        private LocalDateTime dataPagamento;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private OrigemMovimentacaoFinanceira origem = OrigemMovimentacaoFinanceira.MANUAL;

        @OneToOne
        @JoinColumn(name = "venda_id", unique = true)
        private Venda venda;

        public MovimentacaoFinanceira() {
        }

        @PrePersist
        protected void aoCriar() {
            if (dataHora == null) {
                dataHora = LocalDateTime.now();
            }

            if (status == null) {
                status = StatusMovimentacaoFinanceira.PAGO;
            }

            if (origem == null) {
                origem = venda != null
                        ? OrigemMovimentacaoFinanceira.VENDA
                        : OrigemMovimentacaoFinanceira.MANUAL;
            }
        }

        public Long getId() {
            return id;
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

        public BigDecimal getValor() {
            return valor;
        }

        public void setValor(BigDecimal valor) {
            this.valor = valor;
        }

        public LocalDateTime getDataHora() {
            return dataHora;
        }

        public void setDataHora(LocalDateTime dataHora) {
            this.dataHora = dataHora;
        }

        public FormaPagamento getFormaPagamento() {
            return formaPagamento;
        }

        public void setFormaPagamento(FormaPagamento formaPagamento) {
            this.formaPagamento = formaPagamento;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
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

        public LocalDate getVencimento() {
            return vencimento;
        }

        public void setVencimento(LocalDate vencimento) {
            this.vencimento = vencimento;
        }

        public OrigemMovimentacaoFinanceira getOrigem() {
            return origem;
        }

        public void setOrigem(OrigemMovimentacaoFinanceira origem) {
            this.origem = origem;
        }

        public Venda getVenda() {
            return venda;
        }

        public void setVenda(Venda venda) {
            this.venda = venda;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getForma() {
            return forma;
        }

        public void setForma(String forma) {
            this.forma = forma;
        }

        public LocalDateTime getDataPagamento() {
            return dataPagamento;
        }

        public void setDataPagamento(LocalDateTime dataPagamento) {
            this.dataPagamento = dataPagamento;
        }
    }