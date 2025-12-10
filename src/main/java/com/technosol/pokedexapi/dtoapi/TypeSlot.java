package com.technosol.pokedexapi.dtoapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TypeSlot {

    // Position du type (1 = principal, 2 = secondaire)
    private Integer slot;

    // Informations sur le type
    private TypeInfo type;


}