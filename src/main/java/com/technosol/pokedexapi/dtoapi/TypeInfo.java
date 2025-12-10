package com.technosol.pokedexapi.dtoapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TypeInfo {

    // Valeur de la stat (ex: 35 pour HP de Pikachu)
    @JsonProperty("base_stat")
    private Integer baseStat;

    // Informations sur la stat
    private StatInfo stat;


}