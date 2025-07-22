package goblen.studios.project_cards.controller;

import goblen.studios.project_cards.dto.Create.CreateUserDto;
import goblen.studios.project_cards.dto.Return.AuthResponseDto;
import goblen.studios.project_cards.dto.Return.ReturnUserDto;
import goblen.studios.project_cards.service.AuthService;
import goblen.studios.project_cards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestParam String email, @RequestParam String password) {
        return authService.authenticate(email, password)
                .map(token -> {
                    ReturnUserDto userDto = userService.getUserByEmail(email);
                    AuthResponseDto response = new AuthResponseDto(token, userDto);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(401).body(null));
    }

    @PostMapping("/register")
    public ResponseEntity<ReturnUserDto> register(@RequestBody CreateUserDto dto) {
        ReturnUserDto user = authService.register(dto);
        return ResponseEntity.ok(user);
    }
}