package goblen.studios.project_cards.dto.Return;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnDeckDto {

    private Long idDeck;
    private String name;

    private Boolean isPublic;

    private Long idUserCreator;
    private String creatorUserName;

    private Integer numberOfCards;
}
