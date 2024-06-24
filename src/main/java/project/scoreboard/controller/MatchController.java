package project.scoreboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.scoreboard.model.Match;
import project.scoreboard.service.MatchService;
import project.scoreboard.service.ScoreService;

import java.util.HashMap;

@Controller
public class MatchController {

    private final MatchService matchService;
    private final HashMap<Integer,Match> matches = new HashMap<>();
    private final ScoreService scoreService;

    public MatchController(MatchService matchService, ScoreService scoreService) {
        this.matchService = matchService;
        this.scoreService = scoreService;
    }

    @GetMapping("/")
    public String getMain(){
        return "/main";
    }

    @GetMapping("/new-match")
    public String getNew(){
        return "/new-match";
    }

    @PostMapping("/new-match")
    public String create(@RequestParam String player1,
                         @RequestParam String player2){

        Match newMatch = matchService.newMatch(player1, player2);
        matches.put(newMatch.hashCode(),newMatch);

        return "redirect:/match-score/uuid=" + newMatch.hashCode();
    }

    @GetMapping("/match-score/uuid={uuid}")
    public String get(@PathVariable(name = "uuid") String uid, Model model){
        Integer uuid = Integer.parseInt(uid);
        Match currentMatch = matches.get(uuid);
        model.addAttribute("player1", currentMatch.getPlayer1());
        model.addAttribute("player2", currentMatch.getPlayer2());
        model.addAttribute("currentMatch", currentMatch);

        return "/match-score";
    }


    @PostMapping("/match-score/{uuid}")
    public String score(@PathVariable(name = "uuid") String id,
                        @RequestParam Integer playerId
                        ){
        Integer uuid = Integer.parseInt(id);
        Match currentMatch = matches.get(uuid);
        Integer[] score = scoreService.scorePoints(matches, playerId, currentMatch);

        currentMatch.setPlayer1Score(score[0]);
        currentMatch.setPlayer2Score(score[1]);

        return "redirect:/match-score/uuid={uuid}";
    }




    @GetMapping("/matches")
    public String all(Model model){
        model.addAttribute("matches", matchService.findAll());
        return "/matches";
    }





}
