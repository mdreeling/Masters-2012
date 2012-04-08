package doconnor.jpa.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.Club;
import doconnor.jpa.domain.Company;
import doconnor.jpa.domain.Sponsorship;
import doconnor.jpa.service.LeagueManager;

@Component
public class AddSponsor extends AbstractCommand implements Command {
	@Autowired
	public AddSponsor(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
		}

	public void execute() {
		Club club = getClub() ;
		Company company = getCompany() ;
	    System.out.print("Duration (years)? ");
	    int duration = scanner.nextInt();
	    System.out.print("Value (millions)? ");
	    float value = scanner.nextFloat();
	    Sponsorship s = new Sponsorship(club,company,value,duration) ;
	    leagueService.addSponsor(s) ;
	}

	public String help() {
		return "setup a club sponsorship deal with a company";
	}

}
