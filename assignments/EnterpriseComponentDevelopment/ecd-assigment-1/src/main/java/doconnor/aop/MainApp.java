package doconnor.aop;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {

	public static void main(String[] args) {
	   	ConfigurableApplicationContext ctx = 
    		new ClassPathXmlApplicationContext ("applicationContext.xml") ;
	   	ctx.close() ;
	}

}
