package com.technosol.pokedexapi.repository;


import com.technosol.pokedexapi.entity.Card;
import com.technosol.pokedexapi.entity.Rarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    // JpaRepository fournit déjà : save(), findById(), findAll(), delete()...

    // ===== MÉTHODES GÉNÉRÉES AUTOMATIQUEMENT =====
    // Spring comprend le nom de la méthode et génère le SQL !

    // SELECT * FROM cards WHERE is_favorite = true
    List<Card> findByIsFavoriteTrue();

    // SELECT * FROM cards WHERE generation = ?
    List<Card> findByGeneration(Integer generation);

    // SELECT * FROM cards WHERE rarity = ?
    List<Card> findByRarity(Rarity rarity);

    // SELECT * FROM cards WHERE name LIKE '%nom%' (insensible à la casse)
    List<Card> findByNameContainingIgnoreCase(String name);

    // SELECT * FROM cards WHERE owner_id = ?
    List<Card> findByOwnerId(Long ownerId);

    // Vérifier si un utilisateur possède déjà ce Pokémon
    // SELECT * FROM cards WHERE owner_id = ? AND poke_api_id = ?
    Optional<Card> findByOwnerIdAndPokeApiId(Long ownerId, Integer pokeApiId);

    // Compter les cartes d'un utilisateur
    // SELECT COUNT(*) FROM cards WHERE owner_id = ?
    Long countByOwnerId(Long ownerId);

    // ===== REQUÊTE PERSONNALISÉE =====
    // Quand le nom de méthode ne suffit pas, on écrit la requête
    @Query("SELECT c FROM Card c WHERE c.owner.id = :ownerId AND :type MEMBER OF c.types")
    List<Card> findByOwnerIdAndType(Long ownerId, String type);

    Optional<Card> findByPokeApiId(Integer pokeApiId);
}

