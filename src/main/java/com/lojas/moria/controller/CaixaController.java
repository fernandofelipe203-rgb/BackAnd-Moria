package com.lojas.moria.controller;

import com.lojas.moria.dto.CaixaAtualizacaoRequest;
import com.lojas.moria.dto.CaixaRequest;
import com.lojas.moria.dto.CaixaResponse;
import com.lojas.moria.service.CaixaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/caixa")
@CrossOrigin(origins = "http://localhost:5173")
public class CaixaController {

    private final CaixaService service;

    public CaixaController(CaixaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CaixaResponse>> listar() {
        return ResponseEntity.ok(service.listarLancamentos());
    }

    @PostMapping
    public ResponseEntity<CaixaResponse> criar(
            @Valid @RequestBody CaixaRequest request) {
        return ResponseEntity.ok(service.criarLancamento(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CaixaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody CaixaAtualizacaoRequest request) {
        return ResponseEntity.ok(service.atualizarLancamento(id, request));
    }

    @PatchMapping("/{id}/baixar")
    public ResponseEntity<CaixaResponse> baixar(@PathVariable Long id) {
        return ResponseEntity.ok(service.baixarLancamento(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.removerLancamento(id);
        return ResponseEntity.noContent().build();
    }
}