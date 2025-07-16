package goblen.studios.project_cards.controller;

import goblen.studios.project_cards.domain.entities.Deck;
import goblen.studios.project_cards.dto.Return.ReturnDeckUserDto;
import goblen.studios.project_cards.service.DeckUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deck-users")
public class DeckUserController {

    private final DeckUserService deckUserService;

    @Autowired
    public DeckUserController(DeckUserService deckUserService) {
        this.deckUserService = deckUserService;
    }

    // ➕ Adicionar deck ao usuário (copia)
    @PostMapping("/copy")
    public ResponseEntity<ReturnDeckUserDto> copyDeckToUser(
            @RequestParam Long userId,
            @RequestParam Long deckId) {
        ReturnDeckUserDto result = deckUserService.copyDeckToUser(userId, deckId);
        return ResponseEntity.ok(result);
    }

    // 🔍 Buscar todos os decks que o usuário copiou
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReturnDeckUserDto>> getDeckUsersByUser(@PathVariable Long userId) {
        List<ReturnDeckUserDto> result = deckUserService.getDeckUsersByUserId(userId);
        return ResponseEntity.ok(result);
    }

    // 🔍 Buscar todos os usuários que copiaram um deck
    @GetMapping("/deck/{deckId}")
    public ResponseEntity<List<ReturnDeckUserDto>> getDeckUsersByDeck(@PathVariable Long deckId) {
        List<ReturnDeckUserDto> result = deckUserService.getDeckUsersByDeckId(deckId);
        return ResponseEntity.ok(result);
    }

}
