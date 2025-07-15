package goblen.studios.project_cards.mapper;

import goblen.studios.project_cards.domain.entities.User;
import goblen.studios.project_cards.domain.entities.UserCard;
import goblen.studios.project_cards.domain.entities.Card;
import goblen.studios.project_cards.domain.entities.Deck;
import goblen.studios.project_cards.dto.Create.CreateUserCardDto;
import goblen.studios.project_cards.dto.Return.ReturnUserCardDto;
import goblen.studios.project_cards.dto.Update.UpdateUserCardDto;

public class UserCardMapper {

    // Converte CreateUserCardDto para entidade UserCard
    public static UserCard fromCreateToEntity(CreateUserCardDto dto, User user, Card card) {
        return UserCard.builder()
                .user(user)
                .card(card)
                .score(dto.getScore())
                .firstStudyDate(dto.getFirstStudyDate())
                .lastStudyDate(dto.getLastStudyDate())
                .build();
    }

    // Converte entidade UserCard para ReturnUserCardDto
    public static ReturnUserCardDto fromEntityToReturn(UserCard entity) {
        Deck deck = entity.getCard().getDeck();

        return ReturnUserCardDto.builder()
                .id(entity.getId())
                .score(entity.getScore())
                .firstStudyDate(entity.getFirstStudyDate())
                .lastStudyDate(entity.getLastStudyDate())
                .userId(entity.getUser().getIdUser())
                .userName(entity.getUser().getName())
                .cardId(entity.getCard().getIdCard())
                .question(entity.getCard().getQuestion())
                .answer(entity.getCard().getAnswer())
                .deckId(deck != null ? deck.getIdDeck() : null)
                .deckName(deck != null ? deck.getName() : null)
                .build();
    }

    public static UserCard fromUpdateToEntity(UpdateUserCardDto dto, UserCard userCard) {
        // Atualiza campos específicos na entidade existente
        userCard.setScore(dto.getScore());
        userCard.setLastStudyDate(dto.getLastStudyDate());
        return userCard;
    }
}
