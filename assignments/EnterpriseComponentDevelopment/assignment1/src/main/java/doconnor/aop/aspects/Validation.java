package doconnor.aop.aspects;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author Neo
 *
 */
@Aspect
public class Validation {
	@Autowired
	private List<Validator> validators = new ArrayList<Validator>();

	Properties properties = new Properties();

	private final Log logger = LogFactory.getLog(getClass());

	@PostConstruct
	public void loaderrors() {
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
	public void validate(ProceedingJoinPoint joinPoint) throws Throwable {

		final List<Errors> errors = new ArrayList<Errors>();

		for (Object arg : joinPoint.getArgs()) {
			validate(errors, arg);
		}

		joinPoint.proceed();

		logger.debug("Method validation complete");
	}

	private Errors validateWithErrorTagging(final Object arg,
			final Validator validator, final List<Errors> errors) {
		final BindException objErrors = new BindException(arg, arg.getClass()
				.getName());
		validator.validate(arg, objErrors);
		if (objErrors.hasErrors()) {
			errors.add(objErrors);
		}
		return objErrors;
	}

	private void validate(final List<Errors> errors, final Object arg)
			throws Exception {
		for (final Validator validator : getValidators()) {
			if (validator.supports(arg.getClass())) {
				logger.debug(validator.getClass() + " can validate "
						+ arg.getClass());
				validateWithErrorTagging(arg, validator, errors);
			}
		}
	}

	/**
	 * @return Returns the validators.
	 */
	public List<Validator> getValidators() {
		return validators;
	}

	/**
	 * @param validators
	 *            The validators to set.
	 */
	public void setValidators(List<Validator> validators) {
		this.validators = validators;
	}
}
