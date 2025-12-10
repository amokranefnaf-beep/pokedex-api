package com.technosol.pokedexapi.dtoapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OtheSprites {
    @JsonProperty("official-artwork")
    private OfficialArtwork officialArtwork;

}