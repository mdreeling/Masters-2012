package doconnor.aop.aspects;

import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.PropertyUtils;
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

import doconnor.aop.domain.validation.ValidationException;

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
			+ "execution(* doconnor.aop.service.LeagueManagerImpl.setup*(..)) ||"
			+ "execution(* doconnor.aop.service.LeagueManagerImpl.tran*(..))")
	public void modify() {
	}

	@Around("modify()")
	public void validate(ProceedingJoinPoint joinPoint) throws Throwable {

		final List<Errors> errors = new ArrayList<Errors>();

		for (Object arg : joinPoint.getArgs()) {
			validate(errors, arg);
		}

		if (errors.size() > 0) {
			logger.warn("Validation error(s). See below.");
			logger.warn(errors.toString());
			logger.debug("Method validation complete");
			throw new ValidationException(errors);
		}

		joinPoint.proceed();

		logger.debug("Validation succeeded.");
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
				checkArgumentForOtherObjects(arg, errors);
			}
		}
	}

	/**
	 * An Object inspector using AC BeanUtils.
	 *
	 * @param arg
	 * @param errors
	 * @throws Exception
	 */
	private void checkArgumentForOtherObjects(final Object arg,
			final List<Errors> errors) throws Exception {

		final PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(arg.getClass());
		for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			final Object propertyValue = propertyDescriptor.getReadMethod()
					.invoke(arg);
			if ((propertyValue != null)
					&& (isClassCollection(propertyValue.getClass()))) {

				for (final Object propertyElementValue : ((Collection) propertyValue)) {
					for (final Validator validator : getValidators()) {
						if (validator.supports(propertyElementValue.getClass())) {
							logger.debug("** " + validator.getClass()
									+ " can validate the Collection "
									+ propertyElementValue.getClass());
							validateWithErrorTagging(propertyElementValue,
									validator, errors);
							checkArgumentForOtherObjects(propertyElementValue,
									errors);
						} else {
							logger.debug("$$ "
									+ propertyValue.getClass()
									+ " is not validatable as a Collection class right now");
						}
					}
				}
			} else if (propertyValue != null) {
				for (final Validator validator : getValidators()) {
					if (validator.supports(propertyValue.getClass())) {
						logger.debug(validator.getClass() + " can validate "
								+ propertyValue.getClass());
						validateWithErrorTagging(propertyValue, validator,
								errors);
						checkArgumentForOtherObjects(propertyValue, errors);
					} else {
						logger.debug("%% "
								+ propertyValue.getClass()
								+ " is not a Collection or a known domain Object and as such is not currently validatable");
					}
				}
			}
		}
	}

	/**
	 * Ripped from stackOverFlow.
	 *
	 * @param c
	 * @return
	 */
	public static boolean isClassCollection(Class c) {
		return Collection.class.isAssignableFrom(c)
				|| Map.class.isAssignableFrom(c);
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
