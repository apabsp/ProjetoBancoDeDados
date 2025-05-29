package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.ClienteDTO;
import com.biblioteca.backend.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
@CrossOrigin(origins = "http://localhost:5173") // ou "*", se preferir
public class ClienteController {

    @Autowired
    private DatabaseService db;

    @GetMapping("/visualizar")
    public List<ClienteDTO> visualizarClientes() {
        return db.visualizarClientes();
    }

    @PostMapping("/pessoaInserir")
    public String inserirPessoa(@RequestBody ClienteDTO clienteDTO){
        return db.inserirPessoaParaCliente(clienteDTO.getNomePessoa(), clienteDTO.getHistorico());
    }

    @PutMapping("/atualizar")
    public String atualizarCliente(@RequestBody ClienteDTO clienteDTO) {
        return db.atualizarClienteEPessoa(
                clienteDTO.getId(),
                clienteDTO.getNomePessoa(),
                clienteDTO.getHistorico()
        );
    }

    @DeleteMapping("/deletar/{clienteId}")
    public String deletarCliente(@PathVariable String clienteId){
        return db.deletarCliente(clienteId);
    }
}
