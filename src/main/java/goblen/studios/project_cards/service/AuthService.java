package goblen.studios.project_cards.service;

import goblen.studios.project_cards.domain.entities.User;
import goblen.studios.project_cards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ConcurrentHashMap<String, User> tokenStore = new ConcurrentHashMap<>();

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<String> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .map(user -> {
                    String token = UUID.randomUUID().toString();
                    tokenStore.put(token, user);
                    return token;
                });
    }

    public Optional<User> getUserByToken(String token) {
        return Optional.ofNullable(tokenStore.get(token));
    }
}