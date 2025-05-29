package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.EditoraDTO;
import com.biblioteca.backend.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/editora")
public class EditoraController {

    private final DatabaseService databaseService;

    @Autowired
    public EditoraController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/inserir")
    public String inserirEditora(@RequestBody EditoraDTO dto) {
        // generate if there isn't an id cause why not?
        String id = (dto.getId() != null && !dto.getId().isEmpty())
                ? dto.getId()
                : UUID.randomUUID().toString();
        return databaseService.inserirEditora(id, dto.getNome());
    }


    @PostMapping("/delete")
    public String deleteEditora(@RequestBody EditoraDTO dto){
        String editoraName = dto.getNome();
        return databaseService.deletarEditoraPorNome(editoraName);
    }

    @GetMapping("/visualizar")
    public List<String> listarEditoras() {
        return databaseService.listarEditoras();
    }
}
