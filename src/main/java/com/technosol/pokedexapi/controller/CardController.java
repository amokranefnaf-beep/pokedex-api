package com.technosol.pokedexapi.controller;

import com.technosol.pokedexapi.dtoapi.CardRequest;
import com.technosol.pokedexapi.dtoapi.DTO.CardResponse;
import com.technosol.pokedexapi.entity.Card;
import com.technosol.pokedexapi.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController  // Dit que c'est un controller qui renvoie du JSON
@RequestMapping("/api/cards")  // Préfixe pour toutes les routes
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")  // Autorise Angular
public class CardController {

    private final CardService cardService;

    // ===== GET : Récupérer toutes les cartes =====
    // GET http://localhost:8080/api/cards?page=0&size=20&sortBy=addedAt&sortDir=desc
    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<Card>> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "addedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(cardService.getAllCards(page, size, sortBy, sortDir));
    }

    // ===== GET : Récupérer une carte par ID =====
    // GET http://localhost:8080/api/cards/1
    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    // ===== GET : Récupérer les favoris =====
    // GET http://localhost:8080/api/cards/favorites
    @GetMapping("/favorites")
    public ResponseEntity<List<Card>> getFavorites() {
        return ResponseEntity.ok(cardService.getFavorites());
    }

    // ===== GET : Rechercher par nom =====
    // GET http://localhost:8080/api/cards/search?name=pikachu
    @GetMapping("/search")
    public ResponseEntity<List<Card>> searchCards(@RequestParam String name) {
        return ResponseEntity.ok(cardService.searchCards(name));
    }

    // ===== POST : Créer une carte =====
    // POST http://localhost:8080/api/cards
    // Body JSON : { "name": "Pikachu", "pokeApiId": 25, ... }
    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody Card card) {
        Card created = cardService.createCard(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ===== PUT : Modifier une carte =====
    // PUT http://localhost:8080/api/cards/1
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable Long id, @RequestBody Card card) {
        return ResponseEntity.ok(cardService.updateCard(id, card));
    }

    // ===== PATCH : Toggle favori =====
    // PATCH http://localhost:8080/api/cards/1/favorite
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<Card> toggleFavorite(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.toggleFavorite(id));
    }

    // ===== DELETE : Supprimer une carte =====
    // DELETE http://localhost:8080/api/cards/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();  // 204 No Content

    }
}