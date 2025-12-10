package com.technosol.pokedexapi.dtoapi;

import com.technosol.pokedexapi.entity.Rarity;
import jakarta.validation.constraints.*;  // Annotations de validation
import lombok.Data;
import java.util.List;

@Data
public class CardRequest {
    @NotNull(message = "L'ID PokéAPI est obligatoire")
    @Min(value = 1, message = "L'ID doit être >= 1")
    @Max(value = 1025, message = "L'ID doit  être <= 1025")
    private Integer pokeApiId;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit faire entre 2 et 100 caractères")
    private String name;

    @Pattern(regexp = "^https?://.*", message = "L'URL doit commencer par http:// ou https://")
    private String imageUrl;

    @NotEmpty(message = "Au moins un type est requis")
    private List<String> types;

    @Min(value = 1, message = "Génération minimum : 1")
    @Max(value = 9, message = "Génération maximum : 9")
    private Integer generation;

    private Rarity rarity;
}
