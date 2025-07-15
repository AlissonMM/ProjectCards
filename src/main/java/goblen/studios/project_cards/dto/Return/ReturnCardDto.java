package goblen.studios.project_cards.dto.Return;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnCardDto {

    private Long idCard;
    private String question;
    private String answer;

    private Long deckId;
    private String deckName;  // Pode ajudar para o frontend mostrar o nome do deck junto
}