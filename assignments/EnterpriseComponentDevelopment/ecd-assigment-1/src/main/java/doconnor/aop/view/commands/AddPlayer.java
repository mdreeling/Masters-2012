package doconnor.aop.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.aop.domain.Player;
import doconnor.aop.service.LeagueManager;
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
	    long clubNo  = getClubNo() ;
		leagueService.signPlayer(clubNo, player);
 	}

	public String help() {
		return "register a new player with a club ";
	}

}
