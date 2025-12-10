package com.technosol.pokedexapi.dtoapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;



@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class StatInfo {



    // Nom de la stat ("hp", "attack", "defense", "speed"...)
    private String name;

}