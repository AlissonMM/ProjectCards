package goblen.studios.project_cards.dto.Create;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCardDto {

    @NotBlank(message = "Question must not be blank")
    @Size(max = 500, message = "Question must be at most 500 characters")
    private String question;

    @NotBlank(message = "Answer must not be blank")
    @Size(max = 500, message = "Answer must be at most 500 characters")
    private String answer;

    @NotNull(message = "Deck ID must not be null")
    private Long deckId;
}