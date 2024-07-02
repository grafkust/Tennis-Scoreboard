package project.scoreboard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Players")
@NoArgsConstructor
public class Player {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private int id;


    @Column(name = "name")
    @NotBlank
    @Getter @Setter
    private String name;

    public Player(String name) {
        this.name = name;
    }


}
