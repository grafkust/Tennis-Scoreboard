package project.scoreboard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@Getter @Setter
@NoArgsConstructor
public class Score {

    private Object point = 0;

    private int game = 0;

    private int set = 0;

    public Score(Object point, int game, int set) {
        this.point = point;
        this.game = game;
        this.set = set;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return game == score.game && set == score.set && Objects.equals(point, score.point);
    }

}
