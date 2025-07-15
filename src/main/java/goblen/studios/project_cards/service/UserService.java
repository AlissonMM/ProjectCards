package goblen.studios.project_cards.service;

import goblen.studios.project_cards.domain.entities.User;
import goblen.studios.project_cards.dto.Create.CreateUserDto;
import goblen.studios.project_cards.dto.Return.ReturnUserDto;
import goblen.studios.project_cards.mapper.UserMapper;
import goblen.studios.project_cards.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Criar novo usuário
    @Transactional
    public ReturnUserDto createUser(CreateUserDto dto) {
        // Verifica se já existe usuário com mesmo e-mail
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = UserMapper.fromCreateToEntity(dto);
        User saved = userRepository.save(user);
        return UserMapper.fromEntityToReturn(saved);
    }

    // Buscar usuário por ID
    public ReturnUserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + id + " not found"));
        return UserMapper.fromEntityToReturn(user);
    }

    // Buscar usuário por email (exemplo de método auxiliar)
    public ReturnUserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User with email " + email + " not found"));
        return UserMapper.fromEntityToReturn(user);
    }

    // Buscar usuários por nome parcial, case insensitive
    public List<ReturnUserDto> searchUsersByName(String name) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);
        return users.stream()
                .map(UserMapper::fromEntityToReturn)
                .collect(Collectors.toList());
    }

    // Buscar usuários mais engajados (com mais decks associados)
    public List<ReturnUserDto> getMostEngagedUsers() {
        List<User> users = userRepository.findMostEngagedUsers();
        return users.stream()
                .map(UserMapper::fromEntityToReturn)
                .collect(Collectors.toList());
    }
}
