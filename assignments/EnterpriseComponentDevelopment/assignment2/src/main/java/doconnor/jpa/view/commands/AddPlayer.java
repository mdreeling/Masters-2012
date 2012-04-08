package doconnor.jpa.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.Club;
import doconnor.jpa.domain.Player;
import doconnor.jpa.service.LeagueManager;
@Component
public class AddPlayer extends AbstractCommand implements Command {
	@Autowired
	public AddPlayer(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
		System.out.print("Player Name?");
	    String name = scanner.next();
	    Player player = new Player(name) ;
	    Club club  =  getClub() ;
		leagueService.signPlayer(player, club);
 	}

	public String help() {
		return "register a new player with a club ";
	}

}
