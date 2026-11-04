package com.lojas.moria.service;

import com.lojas.moria.execption.ClienteComVendasException;
import com.lojas.moria.execption.ClienteNaoEcontradoException;
import com.lojas.moria.execption.TelefoneClienteDuplicadoException;
import com.lojas.moria.model.Cliente;
import com.lojas.moria.repository.ClienteRepository;
import com.lojas.moria.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VendaRepository vendaRepository;

    public List<Cliente> listarClientes(){
       return clienteRepository.findAll();
    }

    public Cliente CadastrarCliente(Cliente cliente){
        if (clienteRepository.existsByTelefone(cliente.getTelefone())) {
            throw new TelefoneClienteDuplicadoException(
                    "Já existe um cliente cadastrado com esse telefone"
            );
        }
        return clienteRepository.save(cliente);
    }
    public Cliente atualizarCliente(Long id, Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEcontradoException(
                        "Cliente nao encontrado com ID: " + id
                ));

        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setEndereco(cliente.getEndereco());
        clienteExistente.setTelefone(cliente.getTelefone());

        clienteExistente.setCpfCnpj(cliente.getCpfCnpj());
        clienteExistente.setEmail(cliente.getEmail());
        clienteExistente.setCep(cliente.getCep());
        clienteExistente.setNumero(cliente.getNumero());
        clienteExistente.setComplemento(cliente.getComplemento());
        clienteExistente.setBairro(cliente.getBairro());
        clienteExistente.setCidade(cliente.getCidade());
        clienteExistente.setEstado(cliente.getEstado());
        clienteExistente.setObservacoes(cliente.getObservacoes());

        if (clienteRepository.existsByTelefoneAndIdNot(
                cliente.getTelefone(), id)) {

            throw new TelefoneClienteDuplicadoException(
                    "Já existe outro cliente cadastrado com esse telefone"
            );
        }

        return clienteRepository.save(clienteExistente);
    }
     public Cliente buscarClientePorId(Long id){
        return clienteRepository.findById(id).orElseThrow(() -> new ClienteNaoEcontradoException("Cliente nao encontrado com ID: " +id));

     }
    public void excluirCliente(Long id) {

        clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEcontradoException(
                        "Cliente nao encontrado com ID: " + id
                ));

        if (vendaRepository.existsByClienteId(id)) {
            throw new ClienteComVendasException(
                    "Este cliente já tem vendas, ele não pode ser excluído."
            );
        }

        clienteRepository.deleteById(id);
    }

}
