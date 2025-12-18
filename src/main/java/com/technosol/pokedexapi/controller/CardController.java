package com.technosol.pokedexapi.controller;

import com.technosol.pokedexapi.entity.Card;
import com.technosol.pokedexapi.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CardController {

    private final CardService cardService;

    // ===== GET : Toutes les cartes =====
    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<Card>> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "addedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(cardService.getAllCards(page, size, sortBy, sortDir));
    }

    // ===== GET : Carte par ID =====
    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    // ===== GET : Favoris =====
    @GetMapping("/favorites")
    public ResponseEntity<List<Card>> getFavorites() {
        return ResponseEntity.ok(cardService.getFavorites());
    }

    // ===== GET : Recherche =====
    @GetMapping("/search")
    public ResponseEntity<List<Card>> searchCards(@RequestParam String name) {
        return ResponseEntity.ok(cardService.searchCards(name));
    }

    // ===== POST : Créer =====
    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody Card card) {
        Card created = cardService.createCard(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ===== PUT : Modifier =====
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable Long id, @RequestBody Card card) {
        return ResponseEntity.ok(cardService.updateCard(id, card));
    }

    // ===== PATCH : Toggle favori =====
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<Card> toggleFavorite(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.toggleFavorite(id));
    }

    // ===== DELETE =====
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}
