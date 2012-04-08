package doconnor.jpa.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.Agent;
import doconnor.jpa.service.LeagueManager;

@Component
public class AddAgent extends AbstractCommand implements Command {
	@Autowired
	public AddAgent(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}
	
	
	public void execute() {
	    System.out.print("Agent Name?");
	    String name = scanner.next();
	    leagueService.addAgent(new Agent(name)) ;
	}

	public String help() {
		return "Add a new agent";
	}

}
