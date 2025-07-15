package goblen.studios.project_cards.mapper;

import goblen.studios.project_cards.domain.entities.Card;
import goblen.studios.project_cards.domain.entities.Deck;

import goblen.studios.project_cards.domain.entities.UserCard;
import goblen.studios.project_cards.dto.Create.CreateCardDto;
import goblen.studios.project_cards.dto.Return.ReturnCardDto;

public class CardMapper {

    // Agora já recebe o Deck para setar na entidade Card
    public static Card fromCreateToEntity(CreateCardDto dto, Deck deck) {
        return Card.builder()
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .deck(deck)
                .build();
    }

    public static ReturnCardDto fromEntityToReturn(Card card) {
        return ReturnCardDto.builder()
                .idCard(card.getIdCard())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .deckId(card.getDeck() != null ? card.getDeck().getIdDeck() : null)
                .deckName(card.getDeck() != null ? card.getDeck().getName() : null)
                .build();
    }

    public static ReturnCardDto fromUserCardToReturn(UserCard userCard) {
        Card card = userCard.getCard();
        Deck deck = card.getDeck();

        return ReturnCardDto.builder()
                .idCard(card.getIdCard())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .deckId(deck != null ? deck.getIdDeck() : null)
                .deckName(deck != null ? deck.getName() : null)
                .build();
    }
}
