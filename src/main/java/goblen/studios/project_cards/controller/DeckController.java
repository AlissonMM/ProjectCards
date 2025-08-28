package goblen.studios.project_cards.controller;

import goblen.studios.project_cards.domain.entities.Deck;
import goblen.studios.project_cards.dto.Create.CreateDeckDto;
import goblen.studios.project_cards.dto.Return.ReturnDeckDto;
import goblen.studios.project_cards.service.DeckService;
import goblen.studios.project_cards.service.DeckUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/decks")
public class DeckController {

    private final DeckService deckService;
    private final DeckUserService deckUserService;

    @Autowired
    public DeckController(DeckService deckService, DeckUserService deckUserService) {
        this.deckService = deckService;
        this.deckUserService = deckUserService;
    }

    // ➕ Criar novo deck
    @PostMapping
    public ResponseEntity<ReturnDeckDto> createDeck(@RequestBody @Valid CreateDeckDto dto) {
        ReturnDeckDto createdDeck = deckService.createDeck(dto);
        return ResponseEntity.ok(createdDeck);
    }

    // 🔍 Buscar deck por ID
    @GetMapping("/{id}")
    public ResponseEntity<ReturnDeckDto> getDeckById(@PathVariable Long id) {
        ReturnDeckDto deck = deckService.getDeckById(id);
        return ResponseEntity.ok(deck);
    }

    // 👤 Buscar decks por usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReturnDeckDto>> getDecksByUser(@PathVariable Long userId) {
        List<ReturnDeckDto> decks = deckService.getDecksByUser(userId);
        return ResponseEntity.ok(decks);
    }

    // 🌍 Buscar todos os decks públicos
    @GetMapping("/public")
    public ResponseEntity<List<ReturnDeckDto>> getAllPublicDecks() {
        List<ReturnDeckDto> decks = deckService.getAllPublicDecks();
        return ResponseEntity.ok(decks);
    }

    // 🔍 Buscar deck por nome exato
    @GetMapping("/by-name")
    public ResponseEntity<ReturnDeckDto> getDeckByName(@RequestParam String name) {
        ReturnDeckDto deck = deckService.getDeckByName(name);
        return ResponseEntity.ok(deck);
    }

    // 🔍 Buscar decks contendo parte do nome
    @GetMapping("/search")
    public ResponseEntity<List<ReturnDeckDto>> searchDecksByName(@RequestParam String namePart) {
        List<ReturnDeckDto> decks = deckService.searchDecksByName(namePart);
        return ResponseEntity.ok(decks);
    }

    // 🔍 Buscar decks mais populares pelo deckUserService
    //porque o deckUserService tem a lógica de popularidade
    @GetMapping("/popular")
    public ResponseEntity<List<ReturnDeckDto>> getMostPopularDecks() {
        List<ReturnDeckDto> result = deckUserService.getMostPopularDecks();
        return ResponseEntity.ok(result);
    }
}
