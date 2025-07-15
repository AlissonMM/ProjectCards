package goblen.studios.project_cards.mapper;

import goblen.studios.project_cards.domain.entities.Deck;
import goblen.studios.project_cards.domain.entities.User;
import goblen.studios.project_cards.dto.Create.CreateDeckDto;
import goblen.studios.project_cards.dto.Return.ReturnDeckDto;

public class DeckMapper {

    // 🔄 Converte CreateDeckDto + User (criador) para Deck (entidade)
    public static Deck fromCreateToEntity(CreateDeckDto dto, User creator) {
        return Deck.builder()
                .name(dto.getName())
                .idUserCreator(creator)
                .isPublic(dto.getIsPublic())
                .build();
    }

    // 🔄 Converte Deck (entidade) para ReturnDeckDto (resposta para o cliente)
    public static ReturnDeckDto fromEntityToReturn(Deck deck) {
        return ReturnDeckDto.builder()
                .idDeck(deck.getIdDeck())
                .name(deck.getName())
                .isPublic(deck.isPublic())
                .idUserCreator(deck.getIdUserCreator() != null ? deck.getIdUserCreator().getIdUser() : null)
                .creatorUserName(deck.getIdUserCreator() != null ? deck.getIdUserCreator().getName() : null)
                .build();
    }
}
