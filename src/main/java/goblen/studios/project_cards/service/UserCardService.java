package goblen.studios.project_cards.service;

import goblen.studios.project_cards.domain.entities.Card;
import goblen.studios.project_cards.domain.entities.User;
import goblen.studios.project_cards.domain.entities.UserCard;
import goblen.studios.project_cards.dto.Create.CreateUserCardDto;
import goblen.studios.project_cards.dto.Update.UpdateUserCardDto;
import goblen.studios.project_cards.dto.Return.ReturnUserCardDto;
import goblen.studios.project_cards.mapper.UserCardMapper;
import goblen.studios.project_cards.repository.CardRepository;
import goblen.studios.project_cards.repository.UserCardRepository;
import goblen.studios.project_cards.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCardService {

    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    @Autowired
    public UserCardService(UserCardRepository userCardRepository,
                           UserRepository userRepository,
                           CardRepository cardRepository) {
        this.userCardRepository = userCardRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
    }

    public List<ReturnUserCardDto> findBestUsersByCard(Long cardId, int limit) {
        List<UserCard> userCards = userCardRepository.findAllByCard_IdCardOrderByScoreDesc(cardId);
        return userCards.stream()
                .limit(limit)
                .map(UserCardMapper::fromEntityToReturn)
                .collect(Collectors.toList());
    }

    public List<ReturnUserCardDto> getUserCardsByUserAndDeck(Long userId, Long deckId) {
        List<UserCard> userCards = userCardRepository.findAllByUserIdAndDeckIdOrderByScoreDesc(userId, deckId);
        return userCards.stream()
                .map(UserCardMapper::fromEntityToReturn)
                .collect(Collectors.toList());
    }

    public List<ReturnUserCardDto> getUserCardsByUser(Long userId) {
        List<UserCard> userCards = userCardRepository.findAllByUser_IdUserOrderByScoreDesc(userId);
        return userCards.stream()
                .map(UserCardMapper::fromEntityToReturn)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserCard createUserCard(CreateUserCardDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Card card = cardRepository.findById(dto.getCardId())
                .orElseThrow(() -> new RuntimeException("Card not found"));

        UserCard existing = userCardRepository.findByUser_IdUserAndCard_IdCard(user.getIdUser(), card.getIdCard());
        if (existing != null) {
            throw new RuntimeException("UserCard already exists, use update instead");
        }

        UserCard newUserCard = UserCardMapper.fromCreateToEntity(dto, user, card);
        return userCardRepository.save(newUserCard);
    }

    @Transactional
    public UserCard updateUserCard(UpdateUserCardDto dto) {
        UserCard userCard = userCardRepository.findByUser_IdUserAndCard_IdCard(dto.getUserId(), dto.getCardId());
        if (userCard == null) {
            throw new RuntimeException("UserCard not found, use create instead");
        }

        // Atualiza só os campos permitidos
        UserCard userCardToSave = UserCardMapper.fromUpdateToEntity(dto, userCard);
        return userCardRepository.save(userCardToSave);
    }

    @Transactional
    public ReturnUserCardDto markCardAsCorrect(Long userId, Long cardId) {
        UserCard userCard = userCardRepository.findByUser_IdUserAndCard_IdCard(userId, cardId);

        if (userCard == null) {
            throw new RuntimeException("UserCard not found. You must study this card before marking it as correct.");
        }

        int currentScore = userCard.getScore();
        int increment = calculateIncreasingIncrement(currentScore);
        int newScore = Math.min(currentScore + increment, 1000);

        userCard.setScore(newScore);
        userCard.setLastStudyDate(LocalDateTime.now());

        UserCard saved = userCardRepository.save(userCard);
        return UserCardMapper.fromEntityToReturn(saved);
    }

    @Transactional
    public ReturnUserCardDto markCardAsWrong(Long userId, Long cardId) {
        UserCard userCard = userCardRepository.findByUser_IdUserAndCard_IdCard(userId, cardId);
        if (userCard == null) {
            throw new RuntimeException("UserCard not found. You must study this card before marking it as correct.");
        }
            int currentScore = userCard.getScore();
            int decreased = (int) (currentScore * 0.2); // mantém 20%, perde 80%
            userCard.setScore(decreased);
            userCard.setLastStudyDate(LocalDateTime.now());

        UserCard saved = userCardRepository.save(userCard);
        return UserCardMapper.fromEntityToReturn(saved);
    }

    private int calculateIncreasingIncrement(int currentScore) {
        if (currentScore < 100) return 5;
        if (currentScore < 200) return 10;
        if (currentScore < 300) return 20;
        if (currentScore < 500) return 30;
        if (currentScore < 700) return 40;
        if (currentScore < 850) return 60;
        if (currentScore < 950) return 75;
        if (currentScore < 990) return 90;
        return 1000 - currentScore; // só completa o que falta até 1000
    }

}
