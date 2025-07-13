package goblen.studios.project_cards.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "card")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_card")
    private Long idCard;

    @Column(name = "question", length = 500, nullable = false)
    private String question;

    @Column(name = "answer", length = 500, nullable = false)
    private String answer;

    //     Várias cartas para um deck
    @ManyToOne
    @JoinColumn(referencedColumnName ="id_deck", nullable = false)
    private Deck idDeckFk;
}
