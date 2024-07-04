package project.scoreboard.service;

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

@ExtendWith(MockitoExtension.class)
class ScoreServiceTest {

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
        Score playerWonBall = new Score("AD",0,0);
        Score playerLooseBall = new Score(30,0,0);

        Match match = new Match(winner,looser, playerWonBall, playerLooseBall);

        scoreService.keepScore(winner.getId(), match);

        //Enter score expected after goal
        boolean winnerScoreIsCorrect = playerWonBall.equals(new Score(0,1,0));
        boolean looserScoreIsCorrect = playerLooseBall.equals(new Score(0,0,0));

        Assertions.assertTrue(winnerScoreIsCorrect);
        Assertions.assertTrue(looserScoreIsCorrect);
    }

    @Test
    public void keepScoreTestDuringTimeBreak(){

        int winnerSets = 0;
        int looserSets = 1;

        //Start timeBreak
        Score playerWonBall = new Score(40,5,winnerSets);
        Score playerLooseBall = new Score(15,6,looserSets);

        Match match = new Match(winner,looser, playerWonBall, playerLooseBall);

        scoreService.keepScore(winner.getId(), match);

        boolean timeBreakIsStartedByWinner = playerWonBall.equals(new Score(0,0,winnerSets));
        boolean timeBreakIsStartedBeLooser = playerLooseBall.equals(new Score(0,0,looserSets));

        Assertions.assertTrue(timeBreakIsStartedByWinner);
        Assertions.assertTrue(timeBreakIsStartedBeLooser);

        //Assign any pointsScore by TimeBreak
        int winnerPoints = 4;
        int looserPoints = 1;

        playerWonBall.setPoint(winnerPoints);
        playerLooseBall.setPoint(looserPoints);

        scoreService.keepScore(winner.getId(), match);

        boolean winnerScoreIsCorrect;
        boolean looserScoreIsCorrect;
        boolean timeBreakAfterGoalIsGoing = !playerWonBall.getPoint().equals(0);

        if (timeBreakAfterGoalIsGoing) {
            winnerScoreIsCorrect = playerWonBall.equals(new Score(winnerPoints + 1,0,winnerSets));
            looserScoreIsCorrect = playerLooseBall.equals(new Score(looserPoints,0,looserSets));
        }
        else {
            winnerScoreIsCorrect = playerWonBall.equals(new Score(0,0,winnerSets + 1));
            looserScoreIsCorrect = playerLooseBall.equals(new Score(0,0,looserSets));
        }

        Assertions.assertTrue(winnerScoreIsCorrect);
        Assertions.assertTrue(looserScoreIsCorrect);
    }




}
