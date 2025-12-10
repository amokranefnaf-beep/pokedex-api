package com.technosol.pokedexapi.dtoapi.DTO;

import com.technosol.pokedexapi.entity.Card;
import com.technosol.pokedexapi.entity.Rarity;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CardResponse {
    private Long id;
    private Integer pokeApiId;
    private String name;
    private String imageUrl;
    private List<String> types;
    private Integer generation;
    private Rarity rarity;
    private Boolean isFavorite;
    private LocalDateTime addedAt;
    private String ownerUsername;  // Juste le nom, pas l'objet User entier

    // Méthode statique pour convertir Entity → DTO
    public static CardResponse fromEntity(Card card) {
        return CardResponse.builder()
                .id(card.getId())
                .pokeApiId(card.getPokeApiId())
                .name(card.getName())
                .imageUrl(card.getImageUrl())
                .types(card.getTypes())
                .generation(card.getGeneration())
                .rarity(card.getRarity())
                .isFavorite(card.getIsFavorite())
                .addedAt(card.getAddedAt())
                .ownerUsername(card.getOwner() != null ? card.getOwner().getUsername() : null)
                .build();
    }
}
