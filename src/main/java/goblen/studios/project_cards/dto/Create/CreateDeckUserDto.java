package goblen.studios.project_cards.dto.Create;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDeckUserDto {
    @NotNull(message = "User ID must not be null")
    private Long userId;

    @NotNull(message = "Deck ID must not be null")
    private Long deckId;
}

