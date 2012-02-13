package doconnor.aop.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.aop.domain.Club;
import doconnor.aop.service.LeagueManager;
@Component
public class AddClub extends AbstractCommand implements Command {
	@Autowired
	public AddClub(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}
	
	public void execute() {
	    System.out.print("Club Name?");
	    String name = scanner.next();
    	long divisionId = getDivisionNo();
    	Club club = new Club(name) ;
    	leagueService.addClub(club, divisionId) ;
	}

	public String help() {
		return "add new club to division";
	}

}
