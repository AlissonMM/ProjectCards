package goblen.studios.project_cards.repository;

import goblen.studios.project_cards.domain.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByQuestion(String question);
    Optional<Card> findByAnswer(String answer);

    List<Card> findAllByQuestionContaining(String question);

    List<Card> findAllByAnswerContaining(String answer);

    List<Card> findAllByDeck_IdDeck(Long deckId);

    Integer countByDeck_IdDeck(Long deckId);
}
