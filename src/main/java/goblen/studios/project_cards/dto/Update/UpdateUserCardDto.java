package goblen.studios.project_cards.dto.Update;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserCardDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long cardId;

    @NotNull
    private Integer score;

    @NotNull
    private LocalDateTime lastStudyDate;
}
