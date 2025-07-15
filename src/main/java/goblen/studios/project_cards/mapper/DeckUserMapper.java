package goblen.studios.project_cards.mapper;

import goblen.studios.project_cards.domain.entities.DeckUser;
import goblen.studios.project_cards.dto.Create.CreateDeckUserDto;
import goblen.studios.project_cards.dto.Return.ReturnDeckUserDto;
import goblen.studios.project_cards.domain.entities.User;
import goblen.studios.project_cards.domain.entities.Deck;

public class DeckUserMapper {

    // Mapper de CreateDeckUserDto para entidade DeckUser
    public static DeckUser fromCreateToEntity(CreateDeckUserDto dto, User user, Deck deck) {
        return DeckUser.builder()
                .user(user)
                .deck(deck)
                .build();
    }

    // Mapper de entidade DeckUser para ReturnDeckUserDto
    public static ReturnDeckUserDto fromEntityToReturn(DeckUser deckUser) {
        return ReturnDeckUserDto.builder()
                .idDeckUser(deckUser.getIdDeckUser())
                .userId(deckUser.getUser().getIdUser())
                .userName(deckUser.getUser().getName())
                .deckId(deckUser.getDeck().getIdDeck())
                .deckName(deckUser.getDeck().getName())
                .build();
    }
}
