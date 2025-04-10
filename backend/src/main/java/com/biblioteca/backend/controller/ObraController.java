// src/main/java/com/biblioteca/backend/controller/ObraController.java

package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.ObraDTO;
import com.biblioteca.backend.model.Obra;
import com.biblioteca.backend.service.ObraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/obras")
@CrossOrigin(origins = "http://localhost:3000") // Permite chamadas do React local
public class ObraController {

    private final ObraService obraService;

    public ObraController(ObraService obraService) {
        this.obraService = obraService;
    }

    @PostMapping
    public ResponseEntity<String> criarObra(@RequestBody ObraDTO obra) {
        obraService.inserirObra(obra.getCodBarras(), obra.getTitulo(), obra.getAnoLanc(), obra.getGenero());
        return ResponseEntity.ok("Obra inserida com sucesso!");
    }
}
