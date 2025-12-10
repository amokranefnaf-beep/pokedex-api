package com.technosol.pokedexapi.entity;



// Imports nécessaires
import jakarta.persistence.*;  // Les annotations JPA
import lombok.*;              // Pour générer le code automatiquement
import java.time.LocalDateTime;
import java.util.List;

@Entity                    // Dit à Spring que c'est une table
@Table(name = "cards")     // Nom de la table dans la BDD
@Data                      // Lombok génère getters, setters, toString...
@NoArgsConstructor         // Constructeur vide (obligatoire pour JPA)
@AllArgsConstructor        // Constructeur avec tous les champs
@Builder                   // Permet de construire l'objet facilement
public class Card {

    @Id  // Clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;

    // L'ID du Pokémon dans PokéAPI (1 = Bulbizarre, 25 = Pikachu...)
    @Column(nullable = false)  // Obligatoire
    private Integer pokeApiId;

    // Le nom du Pokémon
    @Column(nullable = false, length = 100)
    private String name;

    // URL de l'image
    @Column(length = 500)
    private String imageUrl;

    // Les types du Pokémon (Feu, Eau, etc.)
    // @ElementCollection permet de stocker une liste de String
    @ElementCollection
    @CollectionTable(name = "card_types")  // Crée une table séparée
    private List<String> types;

    // La génération (1 à 9)
    private Integer generation;

    // La rareté de la carte
    /* Stocke le nom (RARE) pas le num&eacute;ro (2) */
    @Enumerated(EnumType.STRING)
    Rarity rarity;

    // Est-ce une carte favorite ?
    @Builder.Default
    private Boolean isFavorite = false;


    // Date d'ajout à la collection
    @Column(nullable = false)
    private LocalDateTime addedAt;

    // Relation avec l'utilisateur propriétaire
    // ManyToOne = Plusieurs cartes peuvent appartenir à 1 utilisateur
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")  // Nom de la colonne FK
    private User owner;

    // Méthode appelée automatiquement avant l'insertion
    @PrePersist
    protected void onCreate() {
        this.addedAt = LocalDateTime.now();
        if (this.isFavorite == null) {
            this.isFavorite = false;
        }
    }
}