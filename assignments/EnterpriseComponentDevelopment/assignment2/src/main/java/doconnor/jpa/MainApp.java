package doconnor.jpa;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {

	public static void main(String[] args) {
        ConfigurableApplicationContext context = 
            new ClassPathXmlApplicationContext("applicationContext.xml");
        context.close() ;
	}
}
