package goblen.studios.project_cards.repository;

import goblen.studios.project_cards.domain.entities.DeckUser;
import goblen.studios.project_cards.domain.entities.Deck;
import goblen.studios.project_cards.domain.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeckUserRepository extends JpaRepository<DeckUser, Integer> {

    // 🔍 Buscar todos os registros onde o usuário é X
    List<DeckUser> findAllByUser_IdUser(Long userId);

    // 🔍 Buscar todos os registros onde o deck é X
    List<DeckUser> findAllByDeck_IdDeck(Long deckId);

    // 📈 Buscar decks mais populares (ordenados pela quantidade de usuários)
    @Query("SELECT du.deck FROM DeckUser du GROUP BY du.deck ORDER BY COUNT(du.user) DESC")
    List<Deck> findMostPopularDecks();

    Integer countByDeck_IdDeck(Long deckId);
}
