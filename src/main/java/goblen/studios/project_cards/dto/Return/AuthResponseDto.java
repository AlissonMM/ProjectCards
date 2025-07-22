package goblen.studios.project_cards.dto.Return;

public class AuthResponseDto {
    private String token;
    private ReturnUserDto user;

    public AuthResponseDto(String token, ReturnUserDto user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }
    public ReturnUserDto getUser() { return user; }
}