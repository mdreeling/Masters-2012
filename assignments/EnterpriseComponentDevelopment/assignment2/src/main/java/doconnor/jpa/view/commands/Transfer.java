package doconnor.jpa.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.Club;
import doconnor.jpa.domain.Player;
import doconnor.jpa.service.LeagueManager;
@Component
public class Transfer extends AbstractCommand implements Command {
	@Autowired
	public Transfer(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
		Player player = getPlayer() ;
		Club club = getClub() ;
    	leagueService.movePlayer(player,  club);
	}

	public String help() {
		return "transfer a player to a new club";
	}

}
