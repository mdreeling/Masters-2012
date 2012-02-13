package doconnor.aop.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.aop.service.LeagueManager;
@Component
public class AddSponsor extends AbstractCommand implements Command {
	@Autowired
	public AddSponsor(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
	    long companyId = getCompanyNo();
	    long clubId = getClubNo();
    	leagueService.addSponsor(clubId, companyId) ;

	}

	public String help() {
		return "add a company to a club's list of sponsors";
	}

}
