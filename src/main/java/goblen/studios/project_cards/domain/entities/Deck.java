package goblen.studios.project_cards.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deck")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_deck")
    private Long idDeck;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id_user", nullable = false)
    private User idUserCreator;


    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

}
