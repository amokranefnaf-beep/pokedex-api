package com.technosol.pokedexapi.dtoapi.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;


/**

 * Réponse de GET /pokemon?limit=X&offset=Y

 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonListResponse {


    // Nombre total de Pokémon
    private Integer count;



    // URL page suivante
    private String next;



    // URL page précédente
    private String previous;



    // Liste des résultats
    private List<PokemonListItem> results;



    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PokemonListItem {

        private String name;

        private String url;  // URL pour obtenir les détails



        // Extrait l'ID depuis l'URL
        // "https://pokeapi.co/api/v2/pokemon/25/" → 25
        public Integer getId() {

            if (url == null) return null;

            String[] parts = url.split("/");

            return Integer.parseInt(parts[parts.length - 1]);

        }

    }

}