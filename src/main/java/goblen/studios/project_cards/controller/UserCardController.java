package goblen.studios.project_cards.controller;

import goblen.studios.project_cards.dto.Create.CreateUserCardDto;
import goblen.studios.project_cards.dto.Return.ReturnCardDto;
import goblen.studios.project_cards.dto.Return.ReturnUserCardDto;
import goblen.studios.project_cards.dto.Update.UpdateUserCardDto;
import goblen.studios.project_cards.domain.entities.UserCard;
import goblen.studios.project_cards.service.UserCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-cards")
public class UserCardController {

    private final UserCardService userCardService;

    @Autowired
    public UserCardController(UserCardService userCardService) {
        this.userCardService = userCardService;
    }



    // ✅ Criar UserCard
    @PostMapping
    public ResponseEntity<ReturnUserCardDto> createUserCard(@RequestBody CreateUserCardDto dto) {
        return ResponseEntity.ok(userCardService.createUserCard(dto));
    }

    // ✅ Atualizar UserCard
    @PutMapping
    public ResponseEntity<ReturnUserCardDto> updateUserCard(@RequestBody UpdateUserCardDto dto) {
        return ResponseEntity.ok(userCardService.updateUserCard(dto));
    }

    // ✅ Buscar melhores usuários por carta
    @GetMapping("/best-users/{cardId}")
    public ResponseEntity<List<ReturnUserCardDto>> getBestUsersByCard(
            @PathVariable Long cardId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(userCardService.findBestUsersByCard(cardId, limit));
    }

    // ✅ Buscar UserCards por usuário e deck
    @GetMapping("/user/{userId}/deck/{deckId}")
    public ResponseEntity<List<ReturnUserCardDto>> getUserCardsByUserAndDeck(
            @PathVariable Long userId,
            @PathVariable Long deckId) {
        return ResponseEntity.ok(userCardService.getUserCardsByUserAndDeck(userId, deckId));
    }

    // ✅ Buscar todos os UserCards de um usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReturnUserCardDto>> getUserCardsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userCardService.getUserCardsByUser(userId));
    }

    // ✅ Marcar carta como correta (ganha pontos)
    @PostMapping("/{userId}/correct/{cardId}")
    public ResponseEntity<ReturnUserCardDto> markCardAsCorrect(
            @PathVariable Long userId,
            @PathVariable Long cardId) {
        return ResponseEntity.ok(userCardService.markCardAsCorrect(userId, cardId));
    }

    // ✅ Marcar carta como errada (perde pontos)
    @PostMapping("/{userId}/wrong/{cardId}")
    public ResponseEntity<ReturnUserCardDto> markCardAsWrong(
            @PathVariable Long userId,
            @PathVariable Long cardId) {
        return ResponseEntity.ok(userCardService.markCardAsWrong(userId, cardId));
    }

    // ✅ Buscar cartas para praticar (mistura cartas com baixo score e aleatórias)
    @GetMapping("/practice")
    public ResponseEntity<List<ReturnCardDto>> practiceCardsByDeck(
            @RequestParam Long userId,
            @RequestParam Long deckId,
            @RequestParam(defaultValue = "0") int limit) {
        return ResponseEntity.ok(userCardService.practiceCardsByDeck(userId, deckId, limit));
    }


}
