package com.technosol.pokedexapi.service;

import com.technosol.pokedexapi.dtoapi.DTO.PokemonApiResponse;
import com.technosol.pokedexapi.entity.Card;
import com.technosol.pokedexapi.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final CardRepository cardRepository;
    private final PokeApiService pokeApiService;

    // Plages d'ID par génération
    private static final Map<Integer, int[]> GENERATION_RANGES = new HashMap<>();
    static {
        GENERATION_RANGES.put(1, new int[]{1, 151});      // Gen 1
        GENERATION_RANGES.put(2, new int[]{152, 251});    // Gen 2
        GENERATION_RANGES.put(3, new int[]{252, 386});    // Gen 3
        GENERATION_RANGES.put(4, new int[]{387, 493});    // Gen 4
        GENERATION_RANGES.put(5, new int[]{494, 649});    // Gen 5
        GENERATION_RANGES.put(6, new int[]{650, 721});    // Gen 6
        GENERATION_RANGES.put(7, new int[]{722, 809});    // Gen 7
        GENERATION_RANGES.put(8, new int[]{810, 905});    // Gen 8
        GENERATION_RANGES.put(9, new int[]{906, 1025});   // Gen 9
    }

    /**
     * Charge une génération complète de Pokémon
     */
    public Map<String, Object> loadGeneration(int generation) {
        if (!GENERATION_RANGES.containsKey(generation)) {
            throw new IllegalArgumentException("Génération invalide. Utilisez 1-9.");
        }

        int[] range = GENERATION_RANGES.get(generation);
        return loadRange(range[0], range[1]);
    }

    /**
     * Charge une plage de Pokémon par ID
     */
    public Map<String, Object> loadRange(int from, int to) {
        log.info("Chargement des Pokémon de {} à {}...", from, to);

        int successCount = 0;
        int skippedCount = 0;
        int errorCount = 0;

        for (int id = from; id <= to; id++) {
            try {
                // Vérifie si le Pokémon existe déjà
                if (cardRepository.findByPokeApiId(id).isPresent()) {
                    skippedCount++;
                    log.debug("Pokémon #{} déjà existant, skip", id);
                    continue;
                }

                Optional<PokemonApiResponse> pokemonOpt = pokeApiService.getPokemonById(id);

                if (pokemonOpt.isPresent()) {
                    Card card = pokeApiService.convertToCard(pokemonOpt.get());
                    cardRepository.save(card);
                    successCount++;
                    log.info("✓ {} ajouté (#{}/{})", card.getName(), id, to);
                } else {
                    errorCount++;
                    log.warn("✗ Pokémon #{} non trouvé", id);
                }

                // Pause pour ne pas surcharger l'API
                Thread.sleep(100);

            } catch (Exception e) {
                errorCount++;
                log.error("✗ Erreur lors du chargement du Pokémon #{}: {}", id, e.getMessage());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", successCount);
        result.put("skipped", skippedCount);
        result.put("errors", errorCount);
        result.put("total", to - from + 1);
        result.put("message", String.format("Chargement terminé : %d ajoutés, %d ignorés, %d erreurs",
            successCount, skippedCount, errorCount));

        log.info("Chargement terminé : {} ajoutés, {} ignorés, {} erreurs",
            successCount, skippedCount, errorCount);

        return result;
    }

    /**
     * Charge tous les Pokémon (1-1025)
     */
    public Map<String, Object> loadAll() {
        return loadRange(1, 1025);
    }

    /**
     * Retourne les statistiques de la base de données
     */
    public Map<String, Object> getStats() {
        long totalCards = cardRepository.count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCards", totalCards);
        stats.put("maxPokemonId", 1025);
        stats.put("coverage", String.format("%.2f%%", (totalCards / 1025.0) * 100));

        return stats;
    }
}
