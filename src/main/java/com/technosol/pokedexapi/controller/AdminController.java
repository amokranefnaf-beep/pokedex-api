package com.technosol.pokedexapi.controller;

import com.technosol.pokedexapi.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private final AdminService adminService;

    /**
     * Charge une génération complète de Pokémon
     * POST http://localhost:8080/api/admin/load-generation/1
     */
    @PostMapping("/load-generation/{generation}")
    public ResponseEntity<Map<String, Object>> loadGeneration(@PathVariable int generation) {
        try {
            Map<String, Object> result = adminService.loadGeneration(generation);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Charge une plage de Pokémon
     * POST http://localhost:8080/api/admin/load-range?from=1&to=151
     */
    @PostMapping("/load-range")
    public ResponseEntity<Map<String, Object>> loadRange(
            @RequestParam int from,
            @RequestParam int to) {
        if (from < 1 || to > 1025 || from > to) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Plage invalide. Utilisez from=1 à to=1025 avec from <= to")
            );
        }

        Map<String, Object> result = adminService.loadRange(from, to);
        return ResponseEntity.ok(result);
    }

    /**
     * Charge tous les Pokémon (1-1025)
     * POST http://localhost:8080/api/admin/load-all
     */
    @PostMapping("/load-all")
    public ResponseEntity<Map<String, Object>> loadAll() {
        Map<String, Object> result = adminService.loadAll();
        return ResponseEntity.ok(result);
    }

    /**
     * Récupère les statistiques de la base de données
     * GET http://localhost:8080/api/admin/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = adminService.getStats();
        return ResponseEntity.ok(stats);
    }
}
