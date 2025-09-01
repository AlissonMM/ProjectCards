package goblen.studios.project_cards.service;

import goblen.studios.project_cards.domain.entities.Deck;
import goblen.studios.project_cards.domain.entities.User;
import goblen.studios.project_cards.dto.Create.CreateDeckDto;
import goblen.studios.project_cards.dto.Return.ReturnDeckDto;
import goblen.studios.project_cards.mapper.DeckMapper;
import goblen.studios.project_cards.repository.CardRepository;
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
public class DeckService {

    private final DeckRepository deckRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final DeckUserRepository deckUserRepository;

    @Autowired
    public DeckService(DeckRepository deckRepository,
                       UserRepository userRepository,
                       CardRepository cardRepository,
                       DeckUserRepository deckUserRepository) {
        this.deckRepository = deckRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.deckUserRepository = deckUserRepository;
    }

    // ✅ Criar novo deck (valida se o usuário existe)
    @Transactional
    public ReturnDeckDto createDeck(CreateDeckDto dto) {
        User creator = userRepository.findById(dto.getIdUserCreator())
                .orElseThrow(() -> new NoSuchElementException("User with ID " + dto.getIdUserCreator() + " not found."));

        Deck deck = DeckMapper.fromCreateToEntity(dto, creator);
        Deck savedDeck = deckRepository.save(deck);

        Integer numberOfCards = cardRepository.countByDeck_IdDeck(savedDeck.getIdDeck());

        Integer numberOfDownloads = deckUserRepository.countByDeck_IdDeck(savedDeck.getIdDeck());

        return DeckMapper.fromEntityToReturn(savedDeck, numberOfCards, numberOfDownloads);
    }

    // ✅ Buscar deck por ID (lança exceção se não existir)
    public ReturnDeckDto getDeckById(Long id) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Deck with ID " + id + " not found."));

        Integer numberOfCards = cardRepository.countByDeck_IdDeck(deck.getIdDeck());

        return DeckMapper.fromEntityToReturn(deck, numberOfCards, numberOfCards);
    }

    // ✅ Buscar todos os decks de um usuário (valida se o usuário existe)
    public List<ReturnDeckDto> getDecksByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with ID " + userId + " not found.");
        }

        return deckRepository.findAllByIdUserCreator_IdUser(userId).stream()
                .map(deck -> {
                    Integer numberOfCards = cardRepository.countByDeck_IdDeck(deck.getIdDeck());
                    Integer numberOfDownloads = deckUserRepository.countByDeck_IdDeck(deck.getIdDeck());
                    return DeckMapper.fromEntityToReturn(deck, numberOfCards, numberOfDownloads);
                } )
                .collect(Collectors.toList());
    }

    // ✅ Buscar todos os decks públicos (se nenhum encontrado, retorna lista vazia)
    public List<ReturnDeckDto> getAllPublicDecks() {
        return deckRepository.findAll().stream()
                .filter(Deck::isPublic)
                .map(deck -> {
                    Integer numberOfCards = cardRepository.countByDeck_IdDeck(deck.getIdDeck());
                    Integer numberOfDownloads = deckUserRepository.countByDeck_IdDeck(deck.getIdDeck());
                    return DeckMapper.fromEntityToReturn(deck, numberOfCards, numberOfDownloads);
                })
                .collect(Collectors.toList());
    }

    // ✅ Buscar deck por nome exato
    public ReturnDeckDto getDeckByName(String name) {
        Deck deck = deckRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Deck with name '" + name + "' not found."));

        Integer numberOfCards = cardRepository.countByDeck_IdDeck(deck.getIdDeck());

        Integer numberOfDownloads = deckUserRepository.countByDeck_IdDeck(deck.getIdDeck());
        return DeckMapper.fromEntityToReturn(deck, numberOfCards, numberOfDownloads);
    }

    // ✅ Buscar decks contendo parte do nome (like)
    public List<ReturnDeckDto> searchDecksByName(String namePart) {
        return deckRepository.findByNameContaining(namePart).stream()
                .map(deck -> {
                    Integer numberOfCards = cardRepository.countByDeck_IdDeck(deck.getIdDeck());
                    Integer numberOfDownloads = deckUserRepository.countByDeck_IdDeck(deck.getIdDeck());
                    return DeckMapper.fromEntityToReturn(deck, numberOfCards, numberOfDownloads);
                })
                .toList();
    }
}
