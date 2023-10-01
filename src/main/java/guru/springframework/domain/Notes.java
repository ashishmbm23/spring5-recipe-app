package guru.springframework.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode( exclude = {"recipe"})
@ToString( exclude = {"recipe"} )
@Entity
public class Notes {
    @OneToOne
    private Recipe recipe;

    @Lob
    private String recipeNotes;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
