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

    @Mock
    private  MatchesRepository matchesRepository;
    @Mock
    private  PlayersRepository playersRepository;

    @InjectMocks
    private ScoreService scoreService;

    private final Player player1 = new Player("PlayerNameOne");
    private final Player player2 = new Player("PlayerNameTwo");

    @Test
    public void assignPointsWith0ScoreTest() {

        Score playerWonBall = new Score(0,0,0);
        Score playerLooseBall = new Score(30,0,0);

        Match match = new Match(player1, player2, playerWonBall, playerLooseBall);

        scoreService.keepScoreAfterGoal(player1.getId(), match);

        Assertions.assertEquals(new Score(15,0,0), playerWonBall);
        Assertions.assertEquals(new Score(30,0,0), playerLooseBall);
    }

    @Test
    public void assignPointsWith15ScoreTest() {

        Score playerWonBall = new Score(15,0,0);
        Score playerLooseBall = new Score(30,0,0);

        Match match = new Match(player1, player2, playerWonBall, playerLooseBall);

        scoreService.keepScoreAfterGoal(player1.getId(), match);

        Assertions.assertEquals(new Score(30,0,0), playerWonBall);
        Assertions.assertEquals(new Score(30,0,0), playerLooseBall);
    }

    @Test
    public void assignPointsWith30ScoreTest() {

        Score playerWonBall = new Score(30,0,0);
        Score playerLooseBall = new Score(30,0,0);

        Match match = new Match(player1, player2, playerWonBall, playerLooseBall);

        scoreService.keepScoreAfterGoal(player1.getId(), match);

        Assertions.assertEquals(new Score(40,0,0), playerWonBall);
        Assertions.assertEquals(new Score(30,0,0), playerLooseBall);
    }


    @Test
    public void assignAdvantageTest() {
        Score playerWonBall = new Score(40,0,0);
        Score playerLooseBall = new Score(40,0,0);

        Match match = new Match(player1, player2, playerWonBall, playerLooseBall);

        scoreService.keepScoreAfterGoal(player1.getId(), match);

        Assertions.assertEquals(new Score("AD",0,0), playerWonBall);
        Assertions.assertEquals(new Score(40,0,0), playerLooseBall);
    }

    @Test
    public void assignGameWithAdvantageTest() {
        Score playerWonBall = new Score("AD",0,0);
        Score playerLooseBall = new Score(40,0,0);

        Match match = new Match(player1, player2, playerWonBall, playerLooseBall);

        scoreService.keepScoreAfterGoal(player1.getId(), match);

        Assertions.assertEquals(new Score(0,1,0), playerWonBall);
        Assertions.assertEquals(new Score(0,0,0), playerLooseBall);
    }

    @Test
    public void assignPointsWhenOpponentHasAdvantageTest() {
        Score playerWonBall = new Score(40,0,0);
        Score playerLooseBall = new Score("AD",0,0);

        Match match = new Match(player1, player2, playerWonBall, playerLooseBall);

        scoreService.keepScoreAfterGoal(player1.getId(), match);

        Assertions.assertEquals(new Score(40,0,0), playerWonBall);
        Assertions.assertEquals(new Score(40,0,0), playerLooseBall);
    }

    @Test
    public void assignGameTest() {
        Score playerWonBall = new Score(40,0,0);
        Score playerLooseBall = new Score(30,0,0);

        Match match = new Match(player1, player2, playerWonBall, playerLooseBall);

        scoreService.keepScoreAfterGoal(player1.getId(), match);

        Assertions.assertEquals(new Score(0,1,0), playerWonBall);
        Assertions.assertEquals(new Score(0,0,0), playerLooseBall);
    }

    @Test
    public void assignGameWhenScore5_5Test(){
        int winnerGames = 5;
        int looserGames = 5;

        Score playerWonBall = new Score(40,winnerGames,0);
        Score playerLooseBall = new Score(30,looserGames,0);

        Match match = new Match(player1, player2, playerWonBall, playerLooseBall);

        scoreService.keepScoreAfterGoal(player1.getId(), match);

        Assertions.assertEquals(new Score(0,winnerGames + 1,0), playerWonBall);
        Assertions.assertEquals(new Score(0,looserGames,0), playerLooseBall);
    }

    @Test
    public void startTimeBreakTest(){
        Score playerWonBall = new Score(40,5,0);
        Score playerLooseBall = new Score(15,6,0);

        Match match = new Match(player1, player2, playerWonBall, playerLooseBall);

        scoreService.keepScoreAfterGoal(player1.getId(), match);

        Assertions.assertFalse(scoreService.timeBreakNotGoing);
    }

    @Test
    public void assignPointsDuringTimeBreakTest(){

        scoreService.timeBreakNotGoing = false;
        scoreService.noCountGamesAfterTimeBreak = true;

        int winnerPoints = 4;
        int looserPoints = 1;

        Score playerWonBall = new Score(winnerPoints,0,0);
        Score playerLooseBall = new Score(looserPoints,0,0);

        Match match = new Match(player1, player2, playerWonBall, playerLooseBall);

        scoreService.keepScoreAfterGoal(player1.getId(), match);

        Assertions.assertEquals(new Score(winnerPoints + 1,0,0), playerWonBall);
        Assertions.assertEquals(new Score(looserPoints,0,0), playerLooseBall);
    }

    @Test
    public void assignSetsAfterTimeBreakTest(){

        scoreService.timeBreakNotGoing = false;
        scoreService.noCountGamesAfterTimeBreak = true;

        int winnerSets = 0;
        int looserSets = 1;

        Score playerWonBall = new Score(6,0,winnerSets);
        Score playerLooseBall = new Score(1,0,looserSets);

        Match match = new Match(player1, player2, playerWonBall, playerLooseBall);

        scoreService.keepScoreAfterGoal(player1.getId(), match);

        Assertions.assertEquals(new Score(0,0,winnerSets + 1), playerWonBall);
        Assertions.assertEquals(new Score(0,0,looserSets), playerLooseBall);
    }

    @Test
    public void assignSetsTest(){
        int winnerSets = 0;
        int looserSets = 1;

        Score playerWonBall = new Score(40,5,winnerSets);
        Score playerLooseBall = new Score(15,4,looserSets);

        Match match = new Match(player1, player2, playerWonBall, playerLooseBall);

        scoreService.keepScoreAfterGoal(player1.getId(), match);

        Assertions.assertEquals(new Score(0,0,winnerSets + 1), playerWonBall);
        Assertions.assertEquals(new Score(0,0,looserSets), playerLooseBall);
    }






}
