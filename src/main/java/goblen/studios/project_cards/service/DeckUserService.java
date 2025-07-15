package goblen.studios.project_cards.service;

import goblen.studios.project_cards.domain.entities.Deck;
import goblen.studios.project_cards.domain.entities.DeckUser;
import goblen.studios.project_cards.domain.entities.User;
import goblen.studios.project_cards.dto.Create.CreateDeckUserDto;
import goblen.studios.project_cards.dto.Return.ReturnDeckUserDto;
import goblen.studios.project_cards.mapper.DeckUserMapper;
import goblen.studios.project_cards.repository.DeckRepository;
import goblen.studios.project_cards.repository.DeckUserRepository;
import goblen.studios.project_cards.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class DeckUserService {

    private final DeckUserRepository deckUserRepository;
    private final UserRepository userRepository;
    private final DeckRepository deckRepository;

    @Autowired
    public DeckUserService(DeckUserRepository deckUserRepository,
                           UserRepository userRepository,
                           DeckRepository deckRepository) {
        this.deckUserRepository = deckUserRepository;
        this.userRepository = userRepository;
        this.deckRepository = deckRepository;
    }

    // Criar associação usuário-deck
    @Transactional
    public ReturnDeckUserDto addUserToDeck(CreateDeckUserDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User with ID " + dto.getUserId() + " not found."));

        Deck deck = deckRepository.findById(dto.getDeckId())
                .orElseThrow(() -> new NoSuchElementException("Deck with ID " + dto.getDeckId() + " not found."));

        DeckUser deckUser = DeckUserMapper.fromCreateToEntity(dto, user, deck);
        DeckUser saved = deckUserRepository.save(deckUser);

        return DeckUserMapper.fromEntityToReturn(saved);
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
    public List<Deck> getMostPopularDecks() {
        return deckUserRepository.findMostPopularDecks();
    }
}
