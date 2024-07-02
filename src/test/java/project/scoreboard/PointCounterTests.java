package project.scoreboard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.scoreboard.model.Match;
import project.scoreboard.model.Player;
import project.scoreboard.model.Score;
import project.scoreboard.repository.MatchesRepository;
import project.scoreboard.repository.PlayersRepository;
import project.scoreboard.service.ScoreService;

@ExtendWith(MockitoExtension.class)
class PointCounterTests {

    @InjectMocks
    private ScoreService scoreService;

    @Mock
    private  MatchesRepository matchesRepository;
    @Mock
    private  PlayersRepository playersRepository;

    private final Player winner = new Player("winnerPlayer");
    private final Player looser = new Player("looserPlayer");

    @Test
    public void keepScoreTestWithoutTimeBreak() {

        //Enter any starting score
        Score winnerScore = new Score(0,0,0);
        Score looserScore = new Score(0,0,0);

        Match match = new Match(winner,looser, winnerScore, looserScore);

        scoreService.keepScore(winner.getId(), match);

        //Enter score expected after goal
        boolean winnerScoreIsCorrect = winnerScore.equals(new Score(15,0,0));
        boolean looserScoreIsCorrect = looserScore.equals(new Score(0,0,0));

        Assertions.assertTrue(winnerScoreIsCorrect);
        Assertions.assertTrue(looserScoreIsCorrect);
    }

    @Test
    public void keepScoreTestDuringTimeBreak(){

        int winnerSets = 1;
        int looserSets = 1;

        //Start timeBreak
        Score winnerScore = new Score(40,5,winnerSets);
        Score looserScore = new Score(15,6,looserSets);

        Match match = new Match(winner,looser, winnerScore, looserScore);

        scoreService.keepScore(winner.getId(), match);

        boolean timeBreakIsStartedByWinner = winnerScore.equals(new Score(0,0,winnerSets));
        boolean timeBreakIsStartedBeLooser = looserScore.equals(new Score(0,0,looserSets));

        Assertions.assertTrue(timeBreakIsStartedByWinner);
        Assertions.assertTrue(timeBreakIsStartedBeLooser);

        //Assign any pointsScore by TimeBreak
        winnerScore.setPoint(1);
        looserScore.setPoint(1);

        scoreService.keepScore(winner.getId(), match);

        boolean winnerScoreIsCorrect = winnerScore.equals(new Score(2,0,winnerSets));
        boolean looserScoreIsCorrect = looserScore.equals(new Score(1,0,looserSets));

        Assertions.assertTrue(winnerScoreIsCorrect);
        Assertions.assertTrue(looserScoreIsCorrect);
    }





}
