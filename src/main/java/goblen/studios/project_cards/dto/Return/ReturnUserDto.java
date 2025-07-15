package goblen.studios.project_cards.dto.Return;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnUserDto {

    private Long idUser;
    private String name;
    private String email;
    private Boolean isPublic;
}
