package goblen.studios.project_cards.service;

import goblen.studios.project_cards.domain.entities.*;
import goblen.studios.project_cards.dto.Create.CreateUserCardDto;
import goblen.studios.project_cards.dto.Return.ReturnCardDto;
import goblen.studios.project_cards.dto.Update.UpdateUserCardDto;
import goblen.studios.project_cards.dto.Return.ReturnUserCardDto;
import goblen.studios.project_cards.mapper.CardMapper;
import goblen.studios.project_cards.mapper.UserCardMapper;
import goblen.studios.project_cards.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class UserCardService {

    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final DeckUserRepository deckUserRepository;
    private final DeckRepository deckRepository;

    @Autowired
    public UserCardService(UserCardRepository userCardRepository,
                           UserRepository userRepository,
                           CardRepository cardRepository,
                           DeckUserRepository deckUserRepository,
                           DeckRepository deckRepository) {
        this.userCardRepository = userCardRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.deckUserRepository = deckUserRepository;
        this.deckRepository = deckRepository;
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

        if (userCard.getFirstStudyDate() == null) {
            userCard.setFirstStudyDate(LocalDateTime.now());
        }

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

        if (userCard.getFirstStudyDate() == null) {
            userCard.setFirstStudyDate(LocalDateTime.now());
        }

            userCard.setLastStudyDate(LocalDateTime.now());



        UserCard saved = userCardRepository.save(userCard);
        return UserCardMapper.fromEntityToReturn(saved);
    }

    @Transactional
    public List<ReturnCardDto> practiceCardsByDeck(Long userId, Long deckId, int limit) {
        // 1. Garante associação DeckUser
        Optional<DeckUser> deckUserOpt = deckUserRepository.findAllByUser_IdUser(userId).stream()
                .filter(du -> du.getDeck().getIdDeck().equals(deckId))
                .findFirst();
        if (deckUserOpt.isEmpty()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Deck deck = deckRepository.findById(deckId)
                    .orElseThrow(() -> new RuntimeException("Deck not found"));
            DeckUser deckUser = DeckUser.builder()
                    .deck(deck)
                    .user(user)
                    .build();
            deckUserRepository.save(deckUser);
        }

        // 2. Garante associação UserCard para todas as cartas do deck
        List<Card> deckCards = cardRepository.findAllByDeck_IdDeck(deckId);
        for (Card card : deckCards) {
            UserCard existing = userCardRepository.findByUser_IdUserAndCard_IdCard(userId, card.getIdCard());
            if (existing == null) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                UserCard userCard = UserCard.builder()
                        .user(user)
                        .card(card)
                        .score(0)
                        .firstStudyDate(null)
                        .lastStudyDate(null)
                        .build();
                userCardRepository.save(userCard);
            }
        }

        // 3. Busca todas as cartas do usuário para o deck, ordenadas por score decrescente
        List<UserCard> userCards = userCardRepository.findAllByUserIdAndDeckIdOrderByScoreDesc(userId, deckId);

        // 4. Filtra as cartas com score menor que 500 (corrigido para lista mutável)
        List<UserCard> lowScore = new ArrayList<>(userCards.stream()
                .filter(uc -> uc.getScore() < 500)
                .toList());

        // 5. Embaralha ambas as listas para evitar viés na seleção
        Collections.shuffle(lowScore);
        List<UserCard> userCardsShufflable = new ArrayList<>(userCards);
        Collections.shuffle(userCardsShufflable);

        // 6. Calcula a quantidade para 60% de cartas com baixo score
        int lowScoreCount = Math.min((int) (limit * 0.6), lowScore.size());

        // 7. Seleciona as cartas com baixo score até o limite calculado
        List<UserCard> selected = new ArrayList<>(lowScore.subList(0, lowScoreCount));
        Set<UserCard> selectedSet = new HashSet<>(selected);

        // 8. Preenche o restante da lista com cartas aleatórias até atingir o limite
        for (UserCard uc : userCardsShufflable) {
            if (selected.size() >= limit) break;
            if (selectedSet.add(uc)) {
                selected.add(uc);
            }
        }

        // 9. Embaralha o resultado final para variar a ordem
        Collections.shuffle(selected);

        // 10. Mapeia as entidades para DTOs de retorno
        return selected.stream()
                .map(CardMapper::fromUserCardToReturn)
                .collect(Collectors.toList());
    }


    private int calculateIncreasingIncrement(int currentScore) {
        if (currentScore < 100){
            int defaultIncrement = ThreadLocalRandom.current().nextInt(10, 50);
            return Math.min(defaultIncrement, 1000 - currentScore);
        }

        if (currentScore < 1000) {
            int minIncrement = currentScore / 2;
            int maxIncrement = currentScore;

            // Define o limite superior como o que falta para chegar a 1000
            int upperBound = Math.min(maxIncrement, 1000 - currentScore);

            //Se o limite mínimo for maior ou igual ao limite superior, significa que
            // já está quase no máximo
            // entao retorna o que falta para completar 1000
            if (minIncrement >= upperBound) {
                return 1000 - currentScore; // já quase no limite
            }
            // Gera um incremento aleatório entre o mínimo e o máximo
            int increment = ThreadLocalRandom.current().nextInt(minIncrement, upperBound);
            return increment;
        }

        return 1000 - currentScore; // só completa o que falta até 1000
    }

}
