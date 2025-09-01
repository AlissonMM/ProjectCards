package goblen.studios.project_cards.service;

import goblen.studios.project_cards.domain.entities.*;
import goblen.studios.project_cards.dto.Create.CreateDeckUserDto;
import goblen.studios.project_cards.dto.Return.ReturnDeckDto;
import goblen.studios.project_cards.dto.Return.ReturnDeckUserDto;
import goblen.studios.project_cards.mapper.DeckMapper;
import goblen.studios.project_cards.mapper.DeckUserMapper;
import goblen.studios.project_cards.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeckUserService {

    private final DeckUserRepository deckUserRepository;
    private final UserRepository userRepository;
    private final DeckRepository deckRepository;
    private final UserCardRepository userCardRepository;
    private final CardRepository cardRepository;

    @Autowired
    public DeckUserService(DeckUserRepository deckUserRepository,
                           UserRepository userRepository,
                           DeckRepository deckRepository,
                           UserCardRepository userCardRepository,
                           CardRepository cardRepository) {
        this.deckUserRepository = deckUserRepository;
        this.userRepository = userRepository;
        this.deckRepository = deckRepository;
        this.userCardRepository = userCardRepository;
        this.cardRepository = cardRepository;
    }

    // Criar associação usuário-deck
    @Transactional
    public ReturnDeckUserDto copyDeckToUser(Long userId, Long deckId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));

        // Verifica se a relação já existe
        Optional<DeckUser> existing = deckUserRepository.findAllByUser_IdUser(userId).stream()
                .filter(du -> du.getDeck().getIdDeck().equals(deckId))
                .findFirst();

        if (existing.isPresent()) {
            throw new RuntimeException("User already associated with this deck");
        }

        // Cria a relação DeckUser
        DeckUser deckUser = DeckUser.builder()
                .deck(deck)
                .user(user)
                .build();
        deckUserRepository.save(deckUser);

        // Criar UserCard para cada carta do deck
        List<Card> deckCards = cardRepository.findAllByDeck_IdDeck(deckId);

        for (Card card : deckCards) {
            boolean exists = userCardRepository.findByUser_IdUserAndCard_IdCard(userId, card.getIdCard()) != null;
            if (!exists) {
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

        return DeckUserMapper.fromEntityToReturn(deckUser);
    }

    // Buscar associações por usuário
    public List<ReturnDeckUserDto> getDeckUsersByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with ID " + userId + " not found.");
        }
        return deckUserRepository.findAllByUser_IdUser(userId).stream()
                .map(DeckUserMapper::fromEntityToReturn)
                .collect(Collectors.toList());
    }

    // Buscar associações por deck
    public List<ReturnDeckUserDto> getDeckUsersByDeckId(Long deckId) {
        if (!deckRepository.existsById(deckId)) {
            throw new NoSuchElementException("Deck with ID " + deckId + " not found.");
        }
        return deckUserRepository.findAllByDeck_IdDeck(deckId).stream()
                .map(DeckUserMapper::fromEntityToReturn)
                .collect(Collectors.toList());
    }

    // Buscar decks mais populares (somente as entidades Deck)
    public List<ReturnDeckDto> getMostPopularDecks() {
        List<Deck> decksList = deckUserRepository.findMostPopularDecks();

        return decksList.stream()
                .map(deck -> {
                    Integer numberOfCards = cardRepository.countByDeck_IdDeck(deck.getIdDeck());
                    return DeckMapper.fromEntityToReturn(deck, numberOfCards);
                }).toList();
    }
}
