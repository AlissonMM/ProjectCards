package goblen.studios.project_cards.dto.Return;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnUserCardDto {

    private Long id;
    private int score;
    private LocalDateTime firstStudyDate;
    private LocalDateTime lastStudyDate;

    private Long userId;
    private String userName;

    private Long cardId;
    private String question;
    private String answer;

    private Long deckId;
    private String deckName;
}
