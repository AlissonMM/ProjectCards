package goblen.studios.project_cards.mapper;

import goblen.studios.project_cards.domain.entities.User;
import goblen.studios.project_cards.dto.Create.CreateUserDto;
import goblen.studios.project_cards.dto.Return.ReturnUserDto;

public class UserMapper {

    public static User fromCreateToEntity(CreateUserDto dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .isPublic(dto.getIsPublic() != null ? dto.getIsPublic() : false)
                .build();
    }

    public static ReturnUserDto fromEntityToReturn(User user) {
        return ReturnUserDto.builder()
                .idUser(user.getIdUser())
                .name(user.getName())
                .email(user.getEmail())
                .isPublic(user.isPublic())
                .build();
    }
}
