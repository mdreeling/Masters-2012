package doconnor.aop.aspects;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import doconnor.aop.domain.validation.ClubValidator;

@Aspect
public class Validation {
	@Autowired
	private final List<Validator> validators = new ArrayList<Validator>();

	Properties properties = new Properties();

	@PostConstruct
	public void loadusers() {
		try {
			properties.load(new FileInputStream("errors.properties"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Pointcut("execution(* doconnor.aop.service.LeagueManagerImpl.add*(..)) || "
			+ "execution(* doconnor.aop.service.LeagueManagerImpl.remove*(..)) || "
			+ "execution(* doconnor.aop.service.LeagueManagerImpl.sign*(..)) || "
			+ "execution(* doconnor.aop.service.LeagueManagerImpl.tran*(..))")
	public void modify() {
	}

	@Around("modify()")
	public void authenticate(ProceedingJoinPoint joinPoint) throws Throwable {


		System.out.println("Before Val");

		for (Object arg : joinPoint.getArgs()) {
			validate(arg);
		}

		joinPoint.proceed();

		System.out.println("After Val");
	}

	private void validate(Object obj) {

		Errors errors = new BindException(obj, obj.getClass().getName());
		Validator validator = new ClubValidator();
		if (validator.supports(obj.getClass())) {
			validator.validate(obj, errors);
		}
		if (errors.hasFieldErrors()) {
			System.out.println(errors.getFieldErrorCount());
		}
	}
}
