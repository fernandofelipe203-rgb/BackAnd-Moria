package com.lojas.moria.model;

import jakarta.persistence.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;
    private String descricao;

    private LocalDateTime dataHora;

    @Valid
    @OneToMany(mappedBy = "movimentacaoEstoque",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ItemMovimentacaoEstoque> itens = new ArrayList<>();

    @NotBlank(message = "A descrição da movimentação é obrigatória")
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public List<ItemMovimentacaoEstoque> getItens() {
        return itens;
    }


    public void setItens(List<ItemMovimentacaoEstoque> itens) {
        this.itens = new ArrayList<>();

        if (itens != null) {
            for (ItemMovimentacaoEstoque item : itens) {
                adicionarItem(item);
            }
        }
    }


    @PrePersist
    public void registrarDataHora() {
        if (dataHora == null) {
            dataHora = LocalDateTime.now();
        }
    }
    public void adicionarItem(ItemMovimentacaoEstoque item) {
        itens.add(item);
        item.setMovimentacaoEstoque(this);
    }
}