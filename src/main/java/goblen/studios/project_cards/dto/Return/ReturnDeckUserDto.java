package goblen.studios.project_cards.dto.Return;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnDeckUserDto {
    private Integer idDeckUser;

    private Long userId;
    private String userName;

    private Long deckId;
    private String deckName;
}
