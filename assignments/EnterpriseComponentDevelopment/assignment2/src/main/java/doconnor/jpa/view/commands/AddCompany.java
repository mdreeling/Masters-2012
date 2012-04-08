package doconnor.jpa.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.Company;
import doconnor.jpa.service.LeagueManager;
@Component
public class AddCompany extends AbstractCommand implements Command {
	
	@Autowired
	public AddCompany(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
	    System.out.print("Company Name?");
	    String name = scanner.next();
	    Company c = new Company(name) ;
	    leagueService.addCompany(c) ;
	}

	public String help() {
		// TODO Auto-generated method stub
		return null;
	}

}
