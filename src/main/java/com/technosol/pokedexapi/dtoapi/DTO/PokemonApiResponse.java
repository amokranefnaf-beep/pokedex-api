package com.technosol.pokedexapi.dtoapi.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * DTO pour mapper la réponse de GET /pokemon/{id}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonApiResponse {

    private Integer id;
    private String name;
    private Integer height;
    private Integer weight;

    private Sprites sprites;
    private List<TypeSlot> types;
    private List<StatSlot> stats;

    // ===== MÉTHODES UTILITAIRES =====

    public String getFormattedName() {
        if (name == null || name.isEmpty()) return name;
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getOfficialArtworkUrl() {
        if (sprites != null && sprites.getOther() != null
                && sprites.getOther().getOfficialArtwork() != null) {
            return sprites.getOther().getOfficialArtwork().getFrontDefault();
        }
        return sprites != null ? sprites.getFrontDefault() : null;
    }

    public List<String> getTypeNames() {
        if (types == null) return List.of();
        return types.stream()
                .map(slot -> slot.getType().getName())
                .toList();
    }

    public Integer getGeneration() {
        if (id == null) return 1;
        if (id <= 151) return 1;
        if (id <= 251) return 2;
        if (id <= 386) return 3;
        if (id <= 493) return 4;
        if (id <= 649) return 5;
        if (id <= 721) return 6;
        if (id <= 809) return 7;
        if (id <= 905) return 8;
        return 9;
    }

    // =============================
    // SOUS-CLASSES MANQUANTES
    // =============================

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sprites {
        @JsonProperty("front_default")
        private String frontDefault;

        private Other other;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Other {
        @JsonProperty("official-artwork")
        private OfficialArtwork officialArtwork;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OfficialArtwork {
        @JsonProperty("front_default")
        private String frontDefault;
    }

    // Types (ex: ["fire", "flying"])
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TypeSlot {
        private int slot;
        private Type type;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Type {
        private String name;
        private String url;
    }

    // Stats
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StatSlot {
        @JsonProperty("base_stat")
        private int baseStat;

        private int effort;
        private Stat stat;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Stat {
        private String name;
        private String url;
    }
}

