package project.scoreboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.scoreboard.model.Match;
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

    public ScoreService(MatchesRepository matchesRepository, PlayersRepository playersRepository) {
        this.matchesRepository = matchesRepository;
        this.playersRepository = playersRepository;
    }


    private Score selectScoreWinner(Integer scoreBallPlayerId,  Match currentMatch) {

        Integer player1Id = currentMatch.getPlayer1().getId();
        if (player1Id.equals(scoreBallPlayerId))
            return currentMatch.getPlayer1Score();
        else
            return currentMatch.getPlayer2Score();
    }

    private Score selectScoreLooser(Integer scoreBallPlayerId, Match currentMatch) {

        Integer player1Id = currentMatch.getPlayer1().getId();

        if (player1Id.equals(scoreBallPlayerId))
            return currentMatch.getPlayer2Score();
        else
            return currentMatch.getPlayer1Score();
    }

    private HashMap<String,Object> countPoints(Object winnerPoints, Object loosePoints) {

        switch (String.valueOf(winnerPoints)) {

            case "0":
                winnerPoints = 15;
                break;
            case "15":
                winnerPoints = 30;
                break;
            case "30":
                winnerPoints = 40;
                break;
            case "40":
                if (!loosePoints.equals(40)) {
                    winnerPoints = 0;
                    loosePoints = 0;

                } else {
                    winnerPoints = "AD";
                    loosePoints = "";
                } break;
            case "": {
                winnerPoints = 40;
                loosePoints = 40;
                break;
            }
            case "AD": {
                winnerPoints = 0;
                loosePoints = 0;

                break;
            }
        }
        HashMap<String, Object> playersPoints = new HashMap<>();
        playersPoints.put("winnerPoints", winnerPoints);
        playersPoints.put("looserPoints", loosePoints);
        return playersPoints;

    }

    private HashMap<String, Integer> countGames(int winnerGames, int looserGames) {
        
        if (winnerGames <5) {
            winnerGames += 1;
        }

        else if (winnerGames == 5 && looserGames == 5 ) {
            winnerGames +=1;
        }


        else if (winnerGames == 5 && looserGames == 6) {
            winnerGames = 10000; //начинается time-break
        }

        else if (winnerGames == 6 && looserGames == 5 || winnerGames == 5 ) {
            winnerGames = 0;
            looserGames = 0;
        }

        HashMap<String, Integer> playersGames = new HashMap<>();
        playersGames.put("winnerGames", winnerGames);
        playersGames.put("looserGames", looserGames);

        return playersGames;

    }

    private HashMap<String, Integer> countSets(int winnerSets, int looserSets) {
        winnerSets += 1;

        HashMap<String, Integer> playersSets = new HashMap<>();
        playersSets.put("winnerSets", winnerSets);
        playersSets.put("looserSets", looserSets);

        return playersSets;
    }



    @Transactional
    public void scorePoints(Integer scoreBallPlayerId, Match currentMatch) {

        Score winnerScore = selectScoreWinner(scoreBallPlayerId, currentMatch);
        Score looseScore = selectScoreLooser(scoreBallPlayerId, currentMatch);

        HashMap<String,Object> playersPoints = countPoints(winnerScore.getPoint(), looseScore.getPoint());
        HashMap<String, Integer> playersGames;
        HashMap<String, Integer> playersSets;

        winnerScore.setPoint(playersPoints.get("winnerPoints"));
        looseScore.setPoint(playersPoints.get("looserPoints"));

        if (playersPoints.get("winnerPoints").equals(0)) {
            playersGames = countGames(winnerScore.getGame(), looseScore.getGame());
            winnerScore.setGame(playersGames.get("winnerGames"));
            looseScore.setGame(playersGames.get("looserGames"));

                if (playersGames.get("winnerGames").equals(0)){
                    playersSets = countSets(winnerScore.getSet(), looseScore.getSet());
                    winnerScore.setSet(playersSets.get("winnerSets"));
                    looseScore.setSet(playersSets.get("looserSets"));
                }
        }











        //1 - playersPoints
        //2 - game
        //3 - set
        //4 - select winner
        
        //Award Game logic
//            //чтобы получить +set необходимо набрать 6 game
//            //1 - набрал 6 game получил set
//            //2 - счет 5:5 ->
//            //  2.1 либо 2 game подряд + set
//            //  2.2 либо счет 6:6 играется time-break.
//            //  В time-break считаются очки по системе 0,1,2 ... ; Для + set необходим перевес в 2 очка.
//            //  Минимальный победный счет 7 очков, например 5 : 7, либо дальнейший счет с перевесом в 2 очка, например 10 : 12
//

    }

//    @Transactional
//    public void scorePoints(Integer scoreBallPlayerId, Match currentMatch) {
//
//        Player player1 = currentMatch.getPlayer1();
//        Player player2 = currentMatch.getPlayer2();
//
//        Score player1Score = currentMatch.getPlayer1Score();
//        Score player2Score = currentMatch.getPlayer2Score();
//
//        int player1Games = player1Score.getGame();
//        int player1Sets = player1Score.getSet();
//        Object player1Points = player1Score.getPoint();
//
//        int player2Games = player2Score.getGame();
//        int player2Sets = player2Score.getSet();
//        Object player2Points = player2Score.getPoint();
//
//        int maxSet = 3;
//
//        //Award points logic
//
//        String[] score;
//        if (scoreBallPlayerId.equals(player1.getId())) {
//            score = pointsCount((String) player1Points, (String) player2Points);
//            player1Score.setPoint(score[0]);
//            player2Score.setPoint(score[1]);
//        }
//        else {
//            score = pointsCount((String) player2Points, (String) player1Points);
//            player1Score.setPoint(score[1]);
//            player2Score.setPoint(score[0]);
//        }
//
//
//            //Award Game logic
//            //чтобы получить +set необходимо набрать 6 game
//            //1 - набрал 6 game получил set
//            //2 - счет 5:5 ->
//            //  2.1 либо 2 game подряд + set
//            //  2.2 либо счет 6:6 играется time-break.
//            //  В time-break считаются очки по системе 0,1,2 ... ; Для + set необходим перевес в 2 очка.
//            //  Минимальный победный счет 7 очков, например 5 : 7, либо дальнейший счет с перевесом в 2 очка, например 10 : 12
//
//
//            //Award game logic
//
//
//            //Define winner logic
////       if (player1Score > maxSet || player2Score > maxSet) {
////            currentMatch.setWinner( (player1Score > maxSet) ? player1 : player2);
////            matchesRepository.save(currentMatch);
////       }
//
//
//        currentMatch.setPlayer1Score(player1Score);
//        currentMatch.setPlayer2Score(player2Score);
//
//
//        }


//
    }








