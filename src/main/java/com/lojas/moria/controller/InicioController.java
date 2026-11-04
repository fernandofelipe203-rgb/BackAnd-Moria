package com.lojas.moria.controller;

import com.lojas.moria.dto.InicioResumoResponse;
import com.lojas.moria.service.InicioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inicio")
public class InicioController {

    private final InicioService inicioService;

    public InicioController(InicioService inicioService) {
        this.inicioService = inicioService;
    }

    @GetMapping("/resumo")
    public InicioResumoResponse buscarResumo() {
        return inicioService.buscarResumo();
    }
}