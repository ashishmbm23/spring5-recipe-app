package guru.springframework.command;

import guru.springframework.domain.Difficulty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand implements Comparable<RecipeCommand>{
    private Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String description;

    @Min(1)
    @Max(999)
    private Integer prepTime;

    @Min(1)
    @Max(999)
    private Integer cookTime;

    @Min(1)
    @Max(100)
    private Integer servings;

    private String source;

    @URL
    private String url;

    @NotBlank
    private String directions;
    private Set<IngredientCommand> ingredients = new TreeSet<>();
    private NotesCommand notes;
    private Difficulty difficlulty;
    private Set<CategoryCommand> categories = new HashSet<>();
    private Byte[] image;

    @Override
    public int compareTo(RecipeCommand recipeCommand) {
        if( this.getId() < recipeCommand.getId() )
            return -1;
        else
            return 1;
    }
}
