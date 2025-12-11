package com.technosol.pokedexapi.service;


import com.technosol.pokedexapi.dtoapi.CardRequest;
import com.technosol.pokedexapi.dtoapi.DTO.PokemonApiResponse;
import com.technosol.pokedexapi.entity.Card;
import com.technosol.pokedexapi.repository.CardRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CardService {

    private final PokeApiService pokeApiService;
    private final CardRepository cardRepository;

    // ===== CREATE =====
    // ===== CREATE =====
    public Card createCard(Card card) {
        // Règle métier : on ne peut pas avoir 2x le même Pokémon
        if (card.getOwner() != null) {
            boolean exists = cardRepository
                    .findByOwnerIdAndPokeApiId(card.getOwner().getId(), card.getPokeApiId())
                    .isPresent();
            if (exists) {
                throw new RuntimeException("Tu possèdes déjà ce Pokémon !");
            }
        }
        return cardRepository.save(card);
    }

    // ===== READ =====
    public Page<Card> getAllCards(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return cardRepository.findAll(pageable);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Card getCardById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carte non trouvée avec l'id: " + id));
    }

    public List<Card> getCardsByOwnerId(Long ownerId) {
        return cardRepository.findByOwnerId(ownerId);
    }

    public List<Card> getFavorites() {
        return cardRepository.findByIsFavoriteTrue();
    }

    public List<Card> searchCards(String name) {
        return cardRepository.findByNameContainingIgnoreCase(name);
    }

    // ===== UPDATE =====
    public Card updateCard(Long id, Card cardDetails) {
        Card card = getCardById(id);  // Récupère la carte existante

        // Met à jour les champs
        card.setName(cardDetails.getName());
        card.setImageUrl(cardDetails.getImageUrl());
        card.setTypes(cardDetails.getTypes());
        card.setGeneration(cardDetails.getGeneration());
        card.setRarity(cardDetails.getRarity());

        return cardRepository.save(card);  // Sauvegarde les modifications
    }

    public Card toggleFavorite(Long id) {
        Card card = getCardById(id);
        card.setIsFavorite(!card.getIsFavorite());  // Inverse la valeur
        return cardRepository.save(card);
    }

    // ===== DELETE =====
    public void deleteCard(Long id) {
        Card card = getCardById(id);  // Vérifie que la carte existe
        cardRepository.delete(card);
    }


    /**
     * Ajoute un Pokémon à la collection en le cherchant sur PokéAPI
     *
     * @param pokeApiId L'ID du Pokémon dans PokéAPI
     * @return La carte créée
     */
    public Card addPokemonToCollection(Integer pokeApiId) {
        // Vérifie qu'on ne l'a pas déjà
        if (cardRepository.findByPokeApiId(pokeApiId).isPresent()) {
            throw new RuntimeException("Tu possèdes déjà ce Pokémon !");
        }

        // Récupère les données depuis PokéAPI
        PokemonApiResponse pokemon = pokeApiService.getPokemonById(pokeApiId)
                .orElseThrow(() -> new RuntimeException("Pokémon non trouvé sur PokéAPI"));

        // Convertit en Card et sauvegarde
        Card card = pokeApiService.convertToCard(pokemon);
        return cardRepository.save(card);
    }

    /**
     * Ajoute un Pokémon par son nom
     */
    public Card addPokemonByName(String name) {
        // Récupère les données depuis PokéAPI
        PokemonApiResponse pokemon = pokeApiService.getPokemonByName(name)
                .orElseThrow(() -> new RuntimeException("Pokémon '" + name + "' non trouvé"));

        // Vérifie qu'on ne l'a pas déjà
        if (cardRepository.findByPokeApiId(pokemon.getId()).isPresent()) {
            throw new RuntimeException("Tu possèdes déjà ce Pokémon !");
        }

        // Convertit en Card et sauvegarde
        Card card = pokeApiService.convertToCard(pokemon);
        return cardRepository.save(card);
    }

}
