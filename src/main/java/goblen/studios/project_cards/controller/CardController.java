package goblen.studios.project_cards.controller;

import goblen.studios.project_cards.domain.entities.Card;
import goblen.studios.project_cards.dto.Create.CreateCardDto;
import goblen.studios.project_cards.dto.Return.ReturnCardDto;
import goblen.studios.project_cards.mapper.CardMapper;
import goblen.studios.project_cards.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    // 🔍 Buscar carta por ID
    @GetMapping("/{id}")
    public ResponseEntity<ReturnCardDto> getCardById(@PathVariable Long id) {
        Optional<Card> cardOpt = cardService.findById(id);
        return cardOpt.map(card -> ResponseEntity.ok(CardMapper.fromEntityToReturn(card)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔍 Buscar por questão exata
    @GetMapping("/by-question")
    public ResponseEntity<ReturnCardDto> getByQuestion(@RequestParam String question) {
        Optional<Card> cardOpt = cardService.findByQuestion(question);
        return cardOpt.map(card -> ResponseEntity.ok(CardMapper.fromEntityToReturn(card)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔍 Buscar por resposta exata
    @GetMapping("/by-answer")
    public ResponseEntity<ReturnCardDto> getByAnswer(@RequestParam String answer) {
        Optional<Card> cardOpt = cardService.findByAnswer(answer);
        return cardOpt.map(card -> ResponseEntity.ok(CardMapper.fromEntityToReturn(card)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔍 Buscar por trecho da questão
    @GetMapping("/search/question")
    public ResponseEntity<List<ReturnCardDto>> searchByQuestion(@RequestParam String keyword) {
        List<Card> cards = cardService.findAllByQuestionContaining(keyword);
        return ResponseEntity.ok(cards.stream().map(CardMapper::fromEntityToReturn).toList());
    }

    // 🔍 Buscar por trecho da resposta
    @GetMapping("/search/answer")
    public ResponseEntity<List<ReturnCardDto>> searchByAnswer(@RequestParam String keyword) {
        List<Card> cards = cardService.findAllByAnswerContaining(keyword);
        return ResponseEntity.ok(cards.stream().map(CardMapper::fromEntityToReturn).toList());
    }

    // 📦 Listar todas as cartas
    @GetMapping
    public ResponseEntity<List<ReturnCardDto>> getAllCards() {
        List<Card> cards = cardService.findAll();
        return ResponseEntity.ok(cards.stream().map(CardMapper::fromEntityToReturn).toList());
    }

    // 📦 Listar cartas por deck
    @GetMapping("/deck/{deckId}")
    public ResponseEntity<List<ReturnCardDto>> getCardsByDeck(@PathVariable Long deckId) {
        List<Card> cards = cardService.findAllByDeckId(deckId);
        return ResponseEntity.ok(cards.stream().map(CardMapper::fromEntityToReturn).toList());
    }

    // ➕ Criar carta
    @PostMapping
    public ResponseEntity<ReturnCardDto> createCard(@RequestBody @Valid CreateCardDto dto) {
        Card savedCard = cardService.createCard(dto);
        return ResponseEntity.ok(CardMapper.fromEntityToReturn(savedCard));
    }

    // ❌ Deletar carta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
