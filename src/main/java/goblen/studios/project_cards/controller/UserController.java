package goblen.studios.project_cards.controller;

import goblen.studios.project_cards.dto.Create.CreateUserDto;
import goblen.studios.project_cards.dto.Return.ReturnUserDto;
import goblen.studios.project_cards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Criar novo usuário
    @PostMapping
    public ResponseEntity<ReturnUserDto> createUser(@RequestBody CreateUserDto dto) {
        ReturnUserDto createdUser = userService.createUser(dto);
        return ResponseEntity.ok(createdUser);
    }

    // ✅ Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<ReturnUserDto> getUserById(@PathVariable Long id) {
        ReturnUserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // ✅ Buscar usuário por email
    @GetMapping("/by-email")
    public ResponseEntity<ReturnUserDto> getUserByEmail(@RequestParam String email) {
        ReturnUserDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    // ✅ Buscar usuários por nome parcial
    @GetMapping("/search")
    public ResponseEntity<List<ReturnUserDto>> searchUsersByName(@RequestParam String name) {
        List<ReturnUserDto> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }

    // ✅ Buscar usuários mais engajados
    @GetMapping("/engaged")
    public ResponseEntity<List<ReturnUserDto>> getMostEngagedUsers() {
        List<ReturnUserDto> users = userService.getMostEngagedUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping
    public ResponseEntity<List<ReturnUserDto>> getAllUsers() {
        List<ReturnUserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
