
package com.lojas.moria.controller;

import com.lojas.moria.dto.VendaRequest;
import com.lojas.moria.dto.VendaResponse;
import com.lojas.moria.model.Venda;
import com.lojas.moria.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public ResponseEntity<VendaResponse> registrarVenda(
            @Valid @RequestBody VendaRequest request) {

        VendaResponse venda = vendaService.registrarVenda(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(venda);
    }
    @GetMapping
    public ResponseEntity<List<VendaResponse>> listarVendas(
            @RequestParam(defaultValue = "5") int ultimas) {

        List<VendaResponse> vendas = vendaService.listarVendas(ultimas);

        return ResponseEntity.ok(vendas);
    }
}
