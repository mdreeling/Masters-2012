package doconnor.jpa.view.commands;

import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.*;
import doconnor.jpa.service.LeagueManager;

@Component
public class AddClub extends AbstractCommand implements Command {
	@Autowired
	public AddClub(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}
	
	public void execute() {
	    System.out.print("Club Name?");
	    String name = scanner.next();
	    System.out.print("Stadium Name?");
	    String sname = scanner.next();
	    System.out.print("Capacity? ");
	    int capacity = scanner.nextInt();
	    System.out.print("Ticket price?");
	    float price = scanner.nextFloat();

    	Division division = getDivision();
    	Club club = new Club(name,new Stadium(sname,capacity,price)) ;
    	leagueService.addClub(club, division) ;
	}

	public String help() {
		return "add new club to division";
	}

}
