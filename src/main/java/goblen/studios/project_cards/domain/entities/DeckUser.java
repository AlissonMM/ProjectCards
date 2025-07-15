package goblen.studios.project_cards.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deck_user")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeckUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_deck_user")
    private int idDeckUser;

    @ManyToOne
    @JoinColumn(name="id_user_fk", referencedColumnName = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="id_deck_fk", referencedColumnName = "id_deck", nullable = false)
    private Deck deck;
}