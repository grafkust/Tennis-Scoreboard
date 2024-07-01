package project.scoreboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.scoreboard.model.Match;
import project.scoreboard.model.Player;
import project.scoreboard.model.Score;
import project.scoreboard.repository.MatchesRepository;
import project.scoreboard.repository.PlayersRepository;

import java.util.HashMap;

@Service
@Transactional(readOnly = true)
public class ScoreService {

    @Autowired
    private final MatchesRepository matchesRepository;
    private final PlayersRepository playersRepository;

    private boolean timeBreakNotGoing = true;
    private final int maxNumberOfSets = 3;
    private boolean noCountGamesAfterTimeBreak = false;

    public ScoreService(MatchesRepository matchesRepository, PlayersRepository playersRepository) {
        this.matchesRepository = matchesRepository;
        this.playersRepository = playersRepository;
    }

    @Transactional
    public void keepScore(int scoredBallPlayerId, Match currentMatch){

        Player scoredBallPlayer = playersRepository.findById(scoredBallPlayerId).get();

        HashMap<String, Score> winnerAndLooserScores = getWinnerAndLooserScores(scoredBallPlayerId, currentMatch);
        Score playerWonBall = winnerAndLooserScores.get("winnerScore");
        Score playerLooseBall = winnerAndLooserScores.get("looserScore");

        countPoints(playerWonBall, playerLooseBall);

        countGames(playerWonBall, playerLooseBall);

        countSets(playerWonBall);

        checkWinnerAndSaveGame(currentMatch, playerWonBall, scoredBallPlayer);
    }

    private boolean checkPlayer1WonBall (Match match, int scoredBallPlayerId){
        Integer player1Id = match.getPlayer1().getId();
        return player1Id.equals(scoredBallPlayerId);
    }

    private HashMap<String, Score> getWinnerAndLooserScores (int scoreBallPlayerId, Match match) {
        HashMap<String, Score> score = new HashMap<>();
        if (checkPlayer1WonBall(match, scoreBallPlayerId)) {
            score.put("winnerScore", match.getPlayer1Score());
            score.put("looserScore", match.getPlayer2Score());
        }
        else {
            score.put("winnerScore", match.getPlayer2Score());
            score.put("looserScore", match.getPlayer1Score());
        }
        return score;
    }

    private void countPoints(Score playerWonBall, Score playerLooseBall)  {

        if (timeBreakNotGoing)
             countPointsWithoutTimeBreak(playerWonBall,playerLooseBall);     //+game
        else
             countPointsDuringTimeBreak(playerWonBall, playerLooseBall);         //+set
    }

    private void countPointsWithoutTimeBreak(Score playerWonBall, Score playerLooseBall){
        Object winnerPoints = playerWonBall.getPoint();
        Object loosePoints = playerLooseBall.getPoint();

        switch (String.valueOf(winnerPoints)) {

            case "0":
                winnerPoints = 15;  break;
            case "15":
                winnerPoints = 30;  break;
            case "30":
                winnerPoints = 40;  break;
            case "40":
                if (!loosePoints.equals(40)) {
                    winnerPoints = 0;
                    loosePoints = 0;
                }
                else {
                    winnerPoints = "AD";
                    loosePoints = "";
                } break;
            case "": {
                winnerPoints = 40;
                loosePoints = 40;   break;
            }
            case "AD": {
                winnerPoints = 0;
                loosePoints = 0;    break;
            }
        }
        playerWonBall.setPoint(winnerPoints);
        playerLooseBall.setPoint(loosePoints);
    }

    private void countPointsDuringTimeBreak(Score playerWonBall, Score playerLooseBall) {

        int winnerPoints = (Integer) playerWonBall.getPoint();
        int looserPoints = (Integer) playerLooseBall.getPoint();
        int pointsDifference = winnerPoints - looserPoints;

        if (winnerPoints < 6 || ( winnerPointsIsEnough(winnerPoints) && !differenceBetweenPointsIsEnough(pointsDifference)) )
            winnerPoints +=1;

        else {
            winnerPoints = 0;
            looserPoints = 0;
            timeBreakNotGoing = true;
            noCountGamesAfterTimeBreak = true;
        }

        playerWonBall.setPoint(winnerPoints);
        playerLooseBall.setPoint(looserPoints);
    }

    private boolean winnerPointsIsEnough(int winnerPoints){
        return winnerPoints >= 6;
    }

    private boolean differenceBetweenPointsIsEnough(int pointsDifference) {
        return pointsDifference >= 1;
    }

    private void countGames(Score playerWonBall, Score playerLooseBall) {

        if (noCountGamesAfterTimeBreak)
            return;

        if (playerWonBall.getPoint().equals(0) && timeBreakNotGoing) {
            int winnerGames = playerWonBall.getGame();
            int looserGames = playerLooseBall.getGame();

            if (winnerGames < 5) {
                winnerGames += 1;
            }
            else if (winnerGames == 5 && looserGames == 5 ) {
                winnerGames +=1;
            }
            else if (winnerGames == 5 && looserGames == 6) {
                winnerGames = 0;
                looserGames = 0;
                timeBreakNotGoing = false;
            }

            else if (winnerGames == 6 && looserGames == 5 || winnerGames == 5 ) {
                winnerGames = 0;
                looserGames = 0;
            }

            playerWonBall.setGame(winnerGames);
            playerLooseBall.setGame(looserGames);
        }
    }

    private void countSets(Score playerWonBall) {

        if (!timeBreakNotGoing)
            return;

        if ( (playerWonBall.getGame() == 0 && playerWonBall.getPoint().equals(0))) {

            int winnerSets = playerWonBall.getSet();

            winnerSets += 1;
            noCountGamesAfterTimeBreak = false;

            playerWonBall.setSet(winnerSets);
        }
    }

    private void checkWinnerAndSaveGame (Match currentMatch, Score playerWonBall, Player winnerScorePlayer) {

        if (playerWonBall.getSet() == maxNumberOfSets){
            currentMatch.setWinner(winnerScorePlayer);
            matchesRepository.save(currentMatch);
        }
    }

}








