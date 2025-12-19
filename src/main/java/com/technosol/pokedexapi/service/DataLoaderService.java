package com.technosol.pokedexapi.service;

import com.technosol.pokedexapi.dtoapi.DTO.PokemonApiResponse;
import com.technosol.pokedexapi.entity.Card;
import com.technosol.pokedexapi.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoaderService implements CommandLineRunner {

    private final CardRepository cardRepository;
    private final PokeApiService pokeApiService;

    @Value("${app.init.pokemon-count:150}")
    private int pokemonCount;

    @Value("${app.init.enabled:true}")
    private boolean enabled;

    // Liste des 150 premiers Pokémon
    private static final int[] POPULAR_POKEMON = generatePokemonIds(150);

    private static int[] generatePokemonIds(int count) {
        int[] ids = new int[count];
        for (int i = 0; i < count; i++) {
            ids[i] = i + 1;
        }
        return ids;
    }

    @Override
    public void run(String... args) {
        if (!enabled) {
            log.info("DataLoader désactivé (app.init.enabled=false)");
            return;
        }

        long existingCount = cardRepository.count();
        if (existingCount > 0) {
            log.info("Base de données déjà initialisée ({} cartes existantes). Skip du chargement.", existingCount);
            return;
        }

        log.info("========================================");
        log.info("Initialisation de la base de données...");
        log.info("Chargement de {} Pokémon depuis PokéAPI", pokemonCount);
        log.info("========================================");

        int successCount = 0;
        int errorCount = 0;

        int limit = Math.min(pokemonCount, POPULAR_POKEMON.length);

        for (int i = 0; i < limit; i++) {
            int pokeApiId = POPULAR_POKEMON[i];
            try {
                Optional<PokemonApiResponse> pokemonOpt = pokeApiService.getPokemonById(pokeApiId);

                if (pokemonOpt.isPresent()) {
                    Card card = pokeApiService.convertToCard(pokemonOpt.get());
                    cardRepository.save(card);
                    successCount++;
                    log.info("[{}/{}] ✓ {} ajouté", successCount, limit, card.getName());
                } else {
                    errorCount++;
                    log.warn("[{}/{}] ✗ Pokémon #{} non trouvé", i + 1, limit, pokeApiId);
                }

                // Petite pause pour ne pas surcharger l'API
                Thread.sleep(100);

            } catch (Exception e) {
                errorCount++;
                log.error("[{}/{}] ✗ Erreur lors du chargement du Pokémon #{}: {}",
                    i + 1, limit, pokeApiId, e.getMessage());
            }
        }

        log.info("========================================");
        log.info("Chargement terminé !");
        log.info("Succès: {} | Erreurs: {}", successCount, errorCount);
        log.info("========================================");
    }
}
