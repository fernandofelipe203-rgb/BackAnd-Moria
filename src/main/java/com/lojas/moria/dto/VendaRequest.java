
package com.lojas.moria.dto;

import com.lojas.moria.model.FormaPagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class VendaRequest {

    @NotNull(message = "Selecione um cliente para realizar a venda")
    private Long clienteId;

    @NotNull(message = "A forma de pagamento é obrigatória")
    private FormaPagamento pagamento;
    @NotNull(message = "A quantidade de parcelas é obrigatória")
    private Integer parcelas = 1;

    @NotNull(message = "A venda deve possuir uma lista de itens")
    @Size(min = 1, message = "A venda deve possuir pelo menos um produto")
    @Valid
    private List<ItemVendaRequest> itens;

    public VendaRequest() {
    }



    public FormaPagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(FormaPagamento pagamento) {
        this.pagamento = pagamento;
    }

    public List<ItemVendaRequest> getItens() {
        return itens;
    }

    public void setItens(List<ItemVendaRequest> itens) {
        this.itens = itens;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public @NotNull(message = "A quantidade de parcelas é obrigatória") Integer getParcelas() {
        return parcelas;
    }

    public void setParcelas(@NotNull(message = "A quantidade de parcelas é obrigatória") Integer parcelas) {
        this.parcelas = parcelas;
    }
}
