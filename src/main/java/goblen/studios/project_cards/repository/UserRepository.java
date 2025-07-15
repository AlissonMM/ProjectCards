package goblen.studios.project_cards.repository;

import goblen.studios.project_cards.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔍 Buscar por e-mail (geralmente usado no login)
    Optional<User> findByEmail(String email);

    // 🔍 Buscar por nome parcial (case-insensitive)
    List<User> findByNameContainingIgnoreCase(String name);

    // 📈 Buscar os usuários com mais decks associados (via tabela deck_user)
    @Query("SELECT du.user FROM DeckUser du GROUP BY du.user ORDER BY COUNT(du.deck) DESC")
    List<User> findMostEngagedUsers();
}
