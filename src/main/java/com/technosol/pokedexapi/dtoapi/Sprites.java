package com.technosol.pokedexapi.dtoapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class Sprites {

    // Image par défaut (sprite pixel art)
    @JsonProperty("front_default")
    private String frontDefault;



    // Autres sprites (dont l'artwork officiel)
    private OtherSprites other;
    private class OtherSprites {
    }
}
