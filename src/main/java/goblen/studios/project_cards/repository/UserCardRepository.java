package goblen.studios.project_cards.repository;

import goblen.studios.project_cards.domain.entities.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, Long> {



    // Buscar todos os UserCards por carta, ordenados pela pontuação desc
    List<UserCard> findAllByCard_IdCardOrderByScoreDesc(Long cardId);

    // Buscar um UserCard específico por usuário e carta (único, não precisa ordenar)
    UserCard findByUser_IdUserAndCard_IdCard(Long userId, Long cardId);


    // Buscar todos os UserCards de um usuário, ordenados por score desc
    List<UserCard> findAllByUser_IdUserOrderByScoreDesc(Long userId);

    @Query("SELECT uc FROM UserCard uc WHERE uc.user.idUser = :userId AND uc.card.deck.idDeck = :deckId ORDER BY uc.score DESC")
    List<UserCard> findAllByUserIdAndDeckIdOrderByScoreDesc(@Param("userId") Long userId, @Param("deckId") Long deckId);
}
