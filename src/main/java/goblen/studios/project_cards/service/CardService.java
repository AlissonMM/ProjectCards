package goblen.studios.project_cards.service;

import goblen.studios.project_cards.domain.entities.Card;
import goblen.studios.project_cards.domain.entities.Deck;
import goblen.studios.project_cards.dto.Create.CreateCardDto;
import goblen.studios.project_cards.mapper.CardMapper;
import goblen.studios.project_cards.repository.CardRepository;
import goblen.studios.project_cards.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final DeckRepository deckRepository;

    @Autowired
    public CardService(CardRepository cardRepository, DeckRepository deckRepository) {
        this.cardRepository = cardRepository;
        this.deckRepository = deckRepository;
    }


    public Optional<Card> findById(Long id) {
        return cardRepository.findById(id);
    }


    public Optional<Card> findByQuestion(String question) {
        return cardRepository.findByQuestion(question);
    }


    public Optional<Card> findByAnswer(String answer) {
        return cardRepository.findByAnswer(answer);
    }


    public List<Card> findAllByQuestionContaining(String keyword) {
        return cardRepository.findAllByQuestionContaining(keyword);
    }


    public List<Card> findAllByAnswerContaining(String keyword) {
        return cardRepository.findAllByAnswerContaining(keyword);
    }


    public Card createCard(CreateCardDto dto) {
        Optional<Deck> deckOpt = deckRepository.findByIdDeck(dto.getDeckId());

        if (deckOpt.isEmpty()) {
            throw new IllegalArgumentException("Deck with ID " + dto.getDeckId() + " does not exist.");
        }

        Deck deck = deckOpt.get();


        Card card = CardMapper.fromCreateToEntity(dto, deck);

        return cardRepository.save(card);
    }



    public void deleteById(Long id) {
        cardRepository.deleteById(id);
    }

    // 📋 Listar todas as cartas
    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public List<Card> findAllByDeckId(Long deckId) {
        return cardRepository.findAllByDeck_IdDeck(deckId);
    }
}
