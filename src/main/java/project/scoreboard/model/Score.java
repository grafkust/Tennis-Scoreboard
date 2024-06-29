package project.scoreboard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
public class Score {

    private Object point = 0;

    private Integer game = 0;

    private Integer set = 0;


}
