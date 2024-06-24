package project.scoreboard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name="Matches")
@Getter @Setter
@NoArgsConstructor
public class Match {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "player1_id", referencedColumnName = "id")
    private Player player1;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "player2_id", referencedColumnName = "id")
    private Player player2;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "winner_id", referencedColumnName = "id")
    private Player winner;

    @Transient
    private Integer player1Score = 0;

    @Transient
    private Integer player2Score = 0;



    public Match(Player player1, Player player2, Integer player1Score, Integer player2Score) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Match(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
    }



    @Override
    public int hashCode() {
        return Objects.hash(id, player1, player2);
    }
}
