package doconnor.aop.view;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.aop.service.LeagueManager;
import doconnor.aop.view.commands.AddClub;
import doconnor.aop.view.commands.AddPlayer;
import doconnor.aop.view.commands.AddSponsor;
import doconnor.aop.view.commands.Bankrupt;
import doconnor.aop.view.commands.ClubsDetail;
import doconnor.aop.view.commands.Command;
import doconnor.aop.view.commands.ListCompanies;
import doconnor.aop.view.commands.Retire;
import doconnor.aop.view.commands.Squad;
import doconnor.aop.view.commands.Transfer;
import doconnor.aop.view.commands.Windup;
@Component("userConsole")
public class AppView {
    private final Scanner scanner;
    private final LeagueManager leagueService ;
  //  @Autowired
    Map<String,Command> commands = new HashMap<String,Command>() ;

    @Autowired
	public AppView(LeagueManager leagueService, Scanner scanner) {
		super();
	    this.scanner = scanner;
	    this.leagueService = leagueService ;
	}

	// @PostConstruct
		public void setupCommands() throws Exception {
			commands.put("addclub", new AddClub(leagueService,scanner)) ;
			commands.put("addsponsor", new AddSponsor(leagueService,scanner)) ;
			commands.put("bankrupt", new Bankrupt(leagueService,scanner)) ;
			commands.put("divinfo", new ClubsDetail(leagueService,scanner)) ;
			commands.put("sponsors", new ListCompanies(leagueService,scanner)) ;
			commands.put("teammates", new Squad(leagueService,scanner)) ;
			commands.put("retire", new Retire(leagueService,scanner)) ;
			commands.put("transfer", new Transfer(leagueService,scanner)) ;
			commands.put("windup", new Windup(leagueService,scanner)) ;
			commands.put("sign", new AddPlayer(leagueService,scanner)) ;
			run() ;
	  }

	public void run() throws Exception
	{
	    System.out.println("League Manager.");
	    System.out.println("Type 'help' for a list of commands.");
	    String request;
	    do
	    {
	    	System.out.print("  --> ");
	        request = scanner.next();
	        Command command = commands.get(request) ;
	        if (command != null) {
	        	command.execute() ;
	        }
	        else if (request.equals("help"))
	        {
	           for (String key : commands.keySet()) {
	        	   System.out.println(key + " = " + commands.get(key).help());
	           }
	        }
	    }
	    while (!(request.equals("quit")));
	    quit();
	    System.out.println("Goodbye.");
	}

	@PreDestroy
	private void quit() throws SQLException {
		leagueService.shutdown();
	}
}

