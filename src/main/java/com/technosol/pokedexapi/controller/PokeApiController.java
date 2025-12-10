package com.technosol.pokedexapi.controller;

import com.technosol.pokedexapi.dtoapi.DTO.CardResponse;
import com.technosol.pokedexapi.dtoapi.DTO.PokemonApiResponse;
import com.technosol.pokedexapi.dtoapi.DTO.PokemonListResponse;
import com.technosol.pokedexapi.entity.Card;
import com.technosol.pokedexapi.service.CardService;
import com.technosol.pokedexapi.service.PokeApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pokeapi")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PokeApiController {
    private final PokeApiService pokeApiService;
    private final CardService cardService;

    // ===== RECHERCHER UN POKÉMON PAR ID =====
    // GET http://localhost:8080/api/pokeapi/pokemon/25
    @GetMapping("/pokemon/{id}")
    public ResponseEntity<PokemonApiResponse> getPokemonById(@PathVariable Integer id) {
        return pokeApiService.getPokemonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== RECHERCHER UN POKÉMON PAR NOM =====
    // GET http://localhost:8080/api/pokeapi/pokemon/search?name=pikachu
    @GetMapping("/pokemon/search")
    public ResponseEntity<PokemonApiResponse> searchPokemon(@RequestParam String name) {
        return pokeApiService.getPokemonByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== LISTE PAGINÉE =====
    // GET http://localhost:8080/api/pokeapi/pokemon?limit=20&offset=0
    @GetMapping("/pokemon")
    public ResponseEntity<PokemonListResponse> listPokemon(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(pokeApiService.getPokemonList(limit, offset));
    }

    // ===== AJOUTER À LA COLLECTION PAR ID =====
    // POST http://localhost:8080/api/pokeapi/pokemon/25/add
    @PostMapping("/pokemon/{id}/add")
    public ResponseEntity<CardResponse> addToCollection(@PathVariable Integer id) {
        Card card = cardService.addPokemonToCollection(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CardResponse.fromEntity(card));
    }

    // ===== AJOUTER À LA COLLECTION PAR NOM =====
    // POST http://localhost:8080/api/pokeapi/pokemon/add?name=pikachu
    @PostMapping("/pokemon/add")
    public ResponseEntity<CardResponse> addByName(@RequestParam String name) {
        Card card = cardService.addPokemonByName(name);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CardResponse.fromEntity(card));
    }
}
