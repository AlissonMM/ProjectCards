package goblen.studios.project_cards.dto.Create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserCardDto {

    @NotNull(message = "User ID must not be null")
    private Long userId;

    @NotNull(message = "Card ID must not be null")
    private Long cardId;

    @PositiveOrZero(message = "Score must be zero or positive")
    private int score;

    @NotNull(message = "First study date must not be null")
    private LocalDateTime firstStudyDate;

    @NotNull(message = "Last study date must not be null")
    private LocalDateTime lastStudyDate;
}
