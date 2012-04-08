package doconnor.jpa.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.Agent;
import doconnor.jpa.domain.Contract;
import doconnor.jpa.domain.Player;
import doconnor.jpa.service.LeagueManager;

@Component
public class SetPlayerAgent extends AbstractCommand implements Command {

	@Autowired
	public SetPlayerAgent(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}
	
	public void execute() {
	    Player player  = getPlayer() ;
	    Agent agent  = getAgent() ;
	    System.out.print("Value (millions)? ");
	    float value = scanner.nextFloat();
	    Contract c = new Contract(agent,player,value) ;
	    leagueService.givePlayerAgent(c);
	}

	public String help() {
		return "Give player an agent";
	}

}
