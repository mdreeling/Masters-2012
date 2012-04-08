package doconnor.jpa.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.view.commands.Command;

@Component("userConsole")
public class AppView {
	@Autowired
    private Scanner scanner;
    @Autowired
    Map<String,Command> commands = new HashMap<String,Command>() ;

	public AppView() {}

	@PostConstruct
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
				try {
					command.execute() ;
				} catch (Exception e) {
					e.printStackTrace();
					System.out
							.println("An error occurred. Resetting system...");
					System.out.println();
				}
	        }
	        else if (request.equals("help"))
	        {
	           for (String key : commands.keySet()) {
	        	   System.out.println(key + " = " + commands.get(key).help());
	           }
	        }
	    }
	    while (!(request.equals("quit")));
	    System.out.println("Goodbye.");
	}
}

