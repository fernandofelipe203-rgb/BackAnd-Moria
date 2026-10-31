
package com.lojas.moria.controller;

import com.lojas.moria.model.MovimentacaoEstoque;
import com.lojas.moria.service.MovimentacaoEstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes-estoque")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    public MovimentacaoEstoqueController(
            MovimentacaoEstoqueService movimentacaoEstoqueService) {
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
    }

    // CREATE: registrar movimentação
    @PostMapping
    public ResponseEntity<String> registrar(
           @Valid @RequestBody MovimentacaoEstoque movimentacao) {

        movimentacaoEstoqueService.registrarMovimentacao(movimentacao);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Movimentação registrada com sucesso");
    }

    // READ: listar movimentações
    @GetMapping
    public ResponseEntity<List<MovimentacaoEstoque>> listarTodas() {
        return ResponseEntity.ok(
                movimentacaoEstoqueService.listarTodas());
    }

    // READ: buscar movimentação por ID
    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoque> buscarPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                movimentacaoEstoqueService.buscarPorId(id));
    }
}
