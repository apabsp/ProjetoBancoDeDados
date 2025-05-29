package com.biblioteca.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.biblioteca.backend.DatabaseInitializer;

@RestController
@RequestMapping("/api")
public class InitController {

    private final DatabaseInitializer initializer;

    public InitController(DatabaseInitializer initializer) {
        this.initializer = initializer;
    }

    @PostMapping("/init")
    public ResponseEntity<String> recreateDB() {
        initializer.init();
        return ResponseEntity.ok("database created successfully!");
    }
}
