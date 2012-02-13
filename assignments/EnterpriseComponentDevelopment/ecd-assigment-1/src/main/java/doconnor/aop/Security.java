package doconnor.aop;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

//@Aspect
public class Security {
	@Autowired
	private Scanner scanner;
	boolean authenticated = false ;
	Properties properties = new Properties();

	@PostConstruct
	public void loadusers() {
		try {
		     properties. load(
					new FileInputStream("users.properties"));
		    } catch (Exception e) {
		           throw new RuntimeException(e);
		    }
	}

    @Pointcut("execution(* doconnor.aop.service.LeagueManagerImpl.add*(..)) || " +
    		  "execution(* doconnor.aop.service.LeagueManagerImpl.remove*(..)) || " +
       		  "execution(* doconnor.aop.service.LeagueManagerImpl.sign*(..)) || " +
    		  "execution(* doconnor.aop.service.LeagueManagerImpl.tran*(..))"
    		   )
    public void modify() {}

    @Around("modify()")
    public void authenticate(ProceedingJoinPoint joinPoint) throws Throwable{
    	if (!authenticated) {
    	   	System.out.println("Username ? ");
    	   	String username = scanner.next() ;
    	   	System.out.println("Password ? ");
    	   	String password = scanner.next() ;
    	   	if (password.equals(properties.get(username))) {
        	   	authenticated = true ;
        	   	joinPoint.proceed() ;
    	   	}
    	   	else {
    	   	   	System.out.println("Invalid user ");
    	   	}
    	}
    	   	else {
        	   	joinPoint.proceed() ;
    	   	}
    	}
}
