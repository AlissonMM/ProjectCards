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
public class CreateDeckDto {

    @NotBlank(message = "Deck name must not be blank")
    @Size(max = 150, message = "Deck name must be at most 150 characters")
    private String name;

    @NotNull(message = "User ID must not be null")
    private Long IdUserCreator;

    @NotNull(message = "isPublic flag must not be null")
    private Boolean isPublic;
}