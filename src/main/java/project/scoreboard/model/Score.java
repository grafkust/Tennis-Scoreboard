package project.scoreboard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
public class Score {

    private Object point = 0;

    private int game = 0;

    private int set = 0;

    public Score(String point, int game, int set) {
        this.point = point;
        this.game = game;
        this.set = set;
    }
}
