package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.EmprestimoDTO;
import com.biblioteca.backend.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID; // Randomizer
import com.biblioteca.backend.dto.ObraDTO;
import com.biblioteca.backend.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/obra")
public class ObraController {

    @Autowired
    private DatabaseService databaseService;

    @PostMapping("/inserir")
    public String inserirObra(@RequestBody ObraDTO obraDTO) {
        String id = obraDTO.getCod_barras();
        String titulo = obraDTO.getTitulo();
        Date anoLanc = obraDTO.getAno_lanc();
        return databaseService.inserirObra(id,titulo, anoLanc);
    }
    /*
        @DeleteMapping("/deletarPorCodBarras")
        public String deletarObraPorCodBarras(@RequestParam String cod_barras) {
            return databaseService.deletarObraPorCodBarras(cod_barras);
        }

        @PutMapping("/atualizar/{cod_barras}")
        public String atualizarObra(@PathVariable String cod_barras, @RequestBody ObraDTO obraDTO) {
            return databaseService.atualizarObra(cod_barras, obraDTO.getTitulo(), obraDTO.getAno_lanc());
        }
        */

    @PutMapping("/alterar")
    public String atualizarObra(@RequestParam String cod_barras, @RequestBody ObraDTO dto) {
        return databaseService.atualizarObra(cod_barras, dto);
    }


    @GetMapping("/visualizar/{cod_barras}")
    public ObraDTO buscarObraPorCodBarras(@PathVariable String cod_barras){
        //System.out.println("getMaps to cod_barras visualizar");
        return databaseService.buscarObraPorCodBarras(cod_barras);
    }

    @GetMapping("/visualizar")
    public List<ObraDTO> visualizarObras() {
        System.out.println("📥 GET /api/obra/visualizar was called");
        return databaseService.visualizarObras();
    }

    @GetMapping("/deletar/{cod_barras}")
    public String deletarObraPorCodBarras(@PathVariable String cod_barras){
        System.out.println("deletar obra por cod barra");
        return databaseService.deletarObraPorCodBarras(cod_barras);
    }


}
