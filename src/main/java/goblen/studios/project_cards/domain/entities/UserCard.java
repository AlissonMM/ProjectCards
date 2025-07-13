package goblen.studios.project_cards.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_card")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_card")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id_card", nullable = false)
    private Card card;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "first_study_date", nullable = false)
    private LocalDateTime firstStudyDate;

    @Column(name = "last_study_date", nullable = false)
    private LocalDateTime lastStudyDate;
}
