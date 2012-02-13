package doconnor.aop.view.commands;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.aop.domain.Company;
import doconnor.aop.service.LeagueManager;
@Component
public class ListCompanies extends AbstractCommand implements Command {
	@Autowired
	public ListCompanies(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
		List<Company> companies = leagueService.listCompanies() ;
		for (Company c : companies)  {
			  System.out.print(c.getName());
			  if (!c.getSupport().isEmpty()) {
				  System.out.print( " **") ;
			  }
			  System.out.println( "") ;
		}
	}

	public String help() {
		return "list all sponsoring companies";
	}

}
