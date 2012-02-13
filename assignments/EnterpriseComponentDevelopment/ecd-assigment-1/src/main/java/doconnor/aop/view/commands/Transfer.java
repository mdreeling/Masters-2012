package doconnor.aop.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.aop.service.LeagueManager;
@Component
public class Transfer extends AbstractCommand implements Command {
	@Autowired
	public Transfer(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
	    long playerId = getPlayerNo();
	    System.out.print("New club ID ?");
	    Long newClubId  = scanner.nextLong();
    	leagueService.transferPlayer(playerId,  newClubId);
	}

	public String help() {
		return "transfer a player to a new club";
	}

}
