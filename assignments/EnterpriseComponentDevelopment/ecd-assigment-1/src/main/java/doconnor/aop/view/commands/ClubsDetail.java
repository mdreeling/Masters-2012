package doconnor.aop.view.commands;

import java.util.Scanner;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.aop.domain.Club;
import doconnor.aop.domain.Player;
import doconnor.aop.service.LeagueManager;
@Component
public class ClubsDetail extends AbstractCommand implements Command {
	@Autowired
	public ClubsDetail(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
		Long divisionId = getDivisionNo();
    	Set<Club> table = leagueService.listDivision(divisionId);
    	for (Club club : table)  {
    	    System.out.println(club.getName());
    	    for (Player player : club.getPlayers()) {
    	    	System.out.println("\t" + player.getName());
    	    }
    	}
	}

	public String help() {
		return "list all clubs and players in a division";
	}

}
