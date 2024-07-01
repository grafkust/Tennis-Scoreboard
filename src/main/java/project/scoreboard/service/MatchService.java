package project.scoreboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.scoreboard.model.Match;
import project.scoreboard.model.Player;
import project.scoreboard.model.Score;
import project.scoreboard.repository.MatchesRepository;
import project.scoreboard.repository.PlayersRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MatchService {

    private final PlayersRepository playersRepository;
    private final MatchesRepository matchesRepository;

    @Autowired
    public MatchService(PlayersRepository playersRepository, MatchesRepository matchesRepository) {
        this.playersRepository = playersRepository;
        this.matchesRepository = matchesRepository;
    }

    private Player findOrSave(String name) {
        Optional<Player> player =  playersRepository.findByName(name);
        if (player.isPresent())
            return player.get();
        else {
            Player newPlayer = new Player(name);
            playersRepository.save(newPlayer);
            return newPlayer;
        }
    }


    @Transactional
    public Match createNewMatch(String player1Name, String player2Name){

        if (player1Name.equals(player2Name))
            throw new IllegalArgumentException();

        Player player1 = findOrSave(player1Name);
        Player players2 = findOrSave(player2Name);

        return new Match(player1, players2, new Score(), new Score());
    }

    public List<Match> findAll(){
        return matchesRepository.findAll();
    }


}
