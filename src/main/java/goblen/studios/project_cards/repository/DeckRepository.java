package goblen.studios.project_cards.repository;

import goblen.studios.project_cards.domain.entities.Deck;
import goblen.studios.project_cards.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {

    Optional<Deck> findByIdDeck(Long id);
    Optional<Deck> findByName (String name);

    Optional<Deck> findByNameContaining(String name);

    // 🔍 Buscar decks criados por um usuário
    List<Deck> findAllByIdUserCreator(User user);

    // 🔍 Ou por ID do usuário
    List<Deck> findAllByIdUserCreator_IdUser(Long userId);
}
