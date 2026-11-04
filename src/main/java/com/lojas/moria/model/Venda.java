
package com.lojas.moria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @NotNull(message = "A forma de pagamento é obrigatória")
    @Enumerated(EnumType.STRING)
    private FormaPagamento pagamento;


    @NotNull(message = "A quantidade de parcelas é obrigatória")
    @PositiveOrZero
    @Column(nullable = false)
    private Integer parcelas = 1;


    @NotNull(message = "O total da venda é obrigatório")
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;


    @OneToMany(
            mappedBy = "venda",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ItemVenda> itens = new ArrayList<>();

    public Venda() {
    }

    @PrePersist
    protected void aoCriar() {
        if (data == null) {
            data = LocalDateTime.now();
        }
    }

    public void adicionarItem(ItemVenda item) {
        itens.add(item);
        item.setVenda(this);
    }

    public void removerItem(ItemVenda item) {
        itens.remove(item);
        item.setVenda(null);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public FormaPagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(FormaPagamento pagamento) {
        this.pagamento = pagamento;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public @NotNull(message = "A quantidade de parcelas é obrigatória") @PositiveOrZero Integer getParcelas() {
        return parcelas;
    }

    public void setParcelas(@NotNull(message = "A quantidade de parcelas é obrigatória") @PositiveOrZero Integer parcelas) {
        this.parcelas = parcelas;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }
}
