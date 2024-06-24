package project.scoreboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.scoreboard.model.Match;
import project.scoreboard.model.Player;
import project.scoreboard.repository.MatchesRepository;

import java.util.HashMap;

@Service
@Transactional(readOnly = true)
public class ScoreService {

    @Autowired
    private final MatchesRepository matchesRepository;

    public ScoreService(MatchesRepository matchesRepository) {
        this.matchesRepository = matchesRepository;
    }


    public Integer[] scorePoints(HashMap<Integer, Match> matches, Integer playerId, Match currentMatch) {

        Integer [] score = new Integer[2];

        Player player1 = currentMatch.getPlayer1();
        Player player2 = currentMatch.getPlayer2();

        int player1Score = currentMatch.getPlayer1Score();
        int player2Score = currentMatch.getPlayer2Score();

        int maxScore = 17;


        //Award points logic

        if (playerId.equals(player1.getId()))
            player1Score += 1;
        else
            player2Score += 1;


        //Check points logic and correct score


        //Define winner logic

        if (player1Score > maxScore){
            appointWinner(currentMatch, player1);
            //возвращаем страничку с именем победителя и финальным счетом
            matches.remove(currentMatch.hashCode());
        }

        if (player2Score > maxScore){
            appointWinner(currentMatch, player2);
            //возвращаем страничку с именем победителя и финальным счетом
            matches.remove(currentMatch.hashCode());
        }


        //return current score
        score[0] = player1Score;
        score[1] = player2Score;

        return score;

    }



    private void appointWinner(Match currentMatch, Player winPlayer) {
        currentMatch.setWinner(winPlayer);
        matchesRepository.save(currentMatch);
    }



}
