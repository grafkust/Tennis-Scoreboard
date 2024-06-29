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
    private Score player1Score;

    @Transient
    private Score player2Score;


    public Match(Player player1, Player player2, Score player1Score, Score player2Score) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
    }



    @Override
    public int hashCode() {
        return Objects.hash(id, player1, player2);
    }
}
