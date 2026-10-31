package com.lojas.moria.controller;

import com.lojas.moria.model.Cliente;
import com.lojas.moria.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> listar(){
    return clienteService.listarClientes();
    }

    @PostMapping
    public Cliente cadastrar(@Valid @RequestBody Cliente cliente){
       return  clienteService.CadastrarCliente(cliente);
    }

    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Long id){
        return clienteService.buscarClientePorId(id);
    }
    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Long id, @RequestBody Cliente cliente){
        return clienteService.atualizarCliente(id, cliente);
    }


    @DeleteMapping("/{id}")
    public String excluir(@PathVariable Long id){
        clienteService.excluirCliente(id);
        return "Cliente excluido com sucesso";
    }

}
