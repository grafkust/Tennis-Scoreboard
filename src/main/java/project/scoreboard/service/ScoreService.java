package project.scoreboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.scoreboard.model.Match;
import project.scoreboard.model.Player;
import project.scoreboard.model.Score;
import project.scoreboard.repository.MatchesRepository;
import project.scoreboard.repository.PlayersRepository;

@Service
@Transactional(readOnly = true)
public class ScoreService {

    @Autowired
    private final MatchesRepository matchesRepository;
    private final PlayersRepository playersRepository;

    private boolean timeBreakNotGoing = true;

    public ScoreService(MatchesRepository matchesRepository, PlayersRepository playersRepository) {
        this.matchesRepository = matchesRepository;
        this.playersRepository = playersRepository;
    }

    @Transactional
    public void scorePoints(Integer scoreBallPlayerId, Match currentMatch) throws Exception {

        Player playerWhoWinPoint = playersRepository.findById(scoreBallPlayerId).get();

        Score winnerScore = selectwinnerScore(scoreBallPlayerId, currentMatch);
        Score looserScore = selectLooserScore(scoreBallPlayerId, currentMatch);

        countPoints(winnerScore, looserScore);

        if (winnerScore.getPoint().equals(0) && timeBreakNotGoing) {
            countGames(winnerScore, looserScore);
        }

//        if ( (winnerScore.getPoint().equals(0) && !timeBreakNotGoing) || winnerScore.getGame().equals(0)) {
//            countSets(winnerScore);
//            checkWinnerAndSaveGame(currentMatch, winnerScore.getGame(),playerWhoWinPoint);
//        }




    }

    private Score selectwinnerScore(Integer scoreBallPlayerId, Match currentMatch) {

        Integer player1Id = currentMatch.getPlayer1().getId();
        if (player1Id.equals(scoreBallPlayerId))
            return currentMatch.getPlayer1Score();
        else
            return currentMatch.getPlayer2Score();
    }

    private Score selectLooserScore(Integer scoreBallPlayerId, Match currentMatch) {

        Integer player1Id = currentMatch.getPlayer1().getId();

        if (player1Id.equals(scoreBallPlayerId))
            return currentMatch.getPlayer2Score();
        else
            return currentMatch.getPlayer1Score();
    }

    private void countPoints(Score winnerPoints, Score loosePoints)  {

        if (timeBreakNotGoing) {
            //+game
             countPointsWithoutTimeBreak(winnerPoints,loosePoints);
        }
        else if (!timeBreakNotGoing) {
            //+set
             countPointsByTimeBreak(winnerPoints, loosePoints);
        }

    }

    private void countPointsWithoutTimeBreak(Score winnerScore, Score looseScore){
        Object winnerPoints = winnerScore.getPoint();
        Object loosePoints = looseScore.getPoint();

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
        winnerScore.setPoint(winnerPoints);
        looseScore.setPoint(loosePoints);
    }

    private void countPointsByTimeBreak (Score winnerScore, Score looseScore) {

        int winnerPoints = (Integer) winnerScore.getPoint();
        int looserPoints = (Integer) looseScore.getPoint();
        int difference = winnerPoints - looserPoints;

        boolean winnerPointsIsEnough = winnerPoints >= 6;
        boolean differenceBetweenPointsIsEnough = difference >=1;

        if (winnerPoints < 6 || (winnerPointsIsEnough && !differenceBetweenPointsIsEnough) )
            winnerPoints +=1;

        else if (winnerPointsIsEnough && differenceBetweenPointsIsEnough) {
            winnerPoints = 0;
            looserPoints = 0;
            timeBreakNotGoing = true;
        }

        winnerScore.setPoint(winnerPoints);
        looseScore.setPoint(looserPoints);

    }

    private void countGames(Score winnerScore, Score looseScore) {

        int winnerGames = winnerScore.getGame();
        int looserGames = looseScore.getGame();

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

        winnerScore.setGame(winnerGames);
        looseScore.setGame(looserGames);

    }

    private void countSets(Score winnerScore) {
        int winnerSets = winnerScore.getSet();

        winnerSets += 1;

        winnerScore.setSet(winnerSets);
    }

    private void checkWinnerAndSaveGame (Match currentMatch, Integer winnerSets, Player winnerScorePlayer) {

        if (winnerSets == 3){
            currentMatch.setWinner(winnerScorePlayer);
            matchesRepository.save(currentMatch);
        }
    }






}








