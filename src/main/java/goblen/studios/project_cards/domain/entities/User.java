package goblen.studios.project_cards.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id_user")
    private Long idUser;

    @Column (name="name", length = 255, nullable = false)
    private String name;
    @Column (name="email", length = 255, nullable = false)
    private String email;

    @JsonIgnore
    @Column (name="password", length = 20, nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<DeckUser> deckUsers;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

}