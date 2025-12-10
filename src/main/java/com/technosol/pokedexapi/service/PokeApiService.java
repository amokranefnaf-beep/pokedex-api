package com.technosol.pokedexapi.service;

import com.technosol.pokedexapi.dtoapi.DTO.PokemonApiResponse;
import com.technosol.pokedexapi.dtoapi.DTO.PokemonListResponse;
import com.technosol.pokedexapi.entity.Card;
import com.technosol.pokedexapi.entity.Rarity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j  // Pour les logs (log.info, log.error...)
public class PokeApiService {

    // URL de base de PokéAPI
    private static final String BASE_URL = "https://pokeapi.co/api/v2";

    // Client HTTP injecté automatiquement
    private final RestTemplate restTemplate;

    // ===== RÉCUPÉRER UN POKÉMON PAR ID =====
    /**
     * Appelle PokéAPI pour récupérer les détails d'un Pokémon
     * @param id L'ID du Pokémon (1-1025)
     * @return Optional contenant le Pokémon, ou vide si non trouvé
     */
    public Optional<PokemonApiResponse> getPokemonById(Integer id) {
        try {
            String url = BASE_URL + "/pokemon/" + id;
            log.info("Appel PokéAPI : {}", url);

            // getForObject = fait un GET et convertit le JSON en objet Java
            PokemonApiResponse response = restTemplate.getForObject(
                    url,
                    PokemonApiResponse.class
            );

            return Optional.ofNullable(response);

        } catch (HttpClientErrorException.NotFound e) {
            // Pokémon non trouvé (erreur 404)
            log.warn("Pokémon {} non trouvé", id);
            return Optional.empty();

        } catch (Exception e) {
            // Autre erreur (réseau, timeout...)
            log.error("Erreur appel PokéAPI : {}", e.getMessage());
            throw new RuntimeException("Erreur lors de l'appel à PokéAPI", e);
        }
    }

    // ===== RÉCUPÉRER UN POKÉMON PAR NOM =====
    public Optional<PokemonApiResponse> getPokemonByName(String name) {
        try {
            // Le nom doit être en minuscules
            String url = BASE_URL + "/pokemon/" + name.toLowerCase().trim();
            log.info("Appel PokéAPI : {}", url);

            PokemonApiResponse response = restTemplate.getForObject(
                    url, PokemonApiResponse.class
            );
            return Optional.ofNullable(response);

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Pokémon '{}' non trouvé", name);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Erreur appel PokéAPI : {}", e.getMessage());
            throw new RuntimeException("Erreur lors de l'appel à PokéAPI", e);
        }
    }

    // ===== LISTE PAGINÉE DE POKÉMON =====
    /**
     * Récupère une liste paginée de Pokémon
     * @param limit Nombre de résultats (max 100)
     * @param offset Point de départ (0 = premier)
     */
    public PokemonListResponse getPokemonList(int limit, int offset) {
        String url = BASE_URL + "/pokemon?limit=" + limit + "&offset=" + offset;
        log.info("Appel PokéAPI : {}", url);

        return restTemplate.getForObject(url, PokemonListResponse.class);
    }

    // ===== CONVERTIR EN ENTITÉ CARD =====
    /**
     * Transforme les données PokéAPI en entité Card
     * pour pouvoir la sauvegarder en base de données
     */
    public Card convertToCard(PokemonApiResponse pokemon) {
        return Card.builder()
                .pokeApiId(pokemon.getId())
                .name(pokemon.getFormattedName())  // Première lettre majuscule
                .imageUrl(pokemon.getOfficialArtworkUrl())
                .types(pokemon.getTypeNames())
                .generation(pokemon.getGeneration())
                .rarity(calculateRarity(pokemon))  // Calcul auto de la rareté
                .isFavorite(false)
                .build();
    }

    // ===== CALCULER LA RARETÉ =====
    /**
     * Détermine la rareté d'un Pokémon selon ses stats
     */
    private Rarity calculateRarity(PokemonApiResponse pokemon) {
        // Calculer la somme des stats
        int totalStats = pokemon.getStats().stream()
                .mapToInt(stat -> stat.getBaseStat())
                .sum();

        // Légendaire : stats totales > 580 (comme Mewtwo, Rayquaza...)
        if (totalStats >= 580) {
            return Rarity.LEGENDARY;
        }
        // Rare : stats > 450
        if (totalStats >= 450) {
            return Rarity.RARE;
        }
        // Peu commun : stats > 350
        if (totalStats >= 350) {
            return Rarity.UNCOMMON;
        }
        // Sinon : commun
        return Rarity.COMMON;
    }
}
