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

	/**
	 * Load errors from properties file.
	 */
	@PostConstruct
	public void loaderrors() {
		try {
			properties.load(new FileInputStream("errors.properties"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * These are pointcuts which cross all 'modification' actions such as adding
	 * Players, signing etc.
	 */
	@Pointcut("execution(* doconnor.aop.service.LeagueManagerImpl.add*(..)) || "
			+ "execution(* doconnor.aop.service.LeagueManagerImpl.remove*(..)) || "
			+ "execution(* doconnor.aop.service.LeagueManagerImpl.sign*(..)) || "
			+ "execution(* doconnor.aop.service.LeagueManagerImpl.setup*(..)) ||"
			+ "execution(* doconnor.aop.service.LeagueManagerImpl.tran*(..))")
	public void modify() {
	}

	/**
	 * The is the method which is going to act on each point cut when its
	 * intercepted.
	 *
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Around("modify()")
	public void validate(ProceedingJoinPoint joinPoint) throws Throwable {

		/**
		 * A list of all of the errors.
		 */
		final List<Errors> errors = new ArrayList<Errors>();

		/**
		 * For each argument passed into the intercepted method.
		 */
		for (Object arg : joinPoint.getArgs()) {
			/**
			 * Validate the argument.
			 */
			validateArgument(errors, arg);
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

	/**
	 * The main validation method. This validates an object and tags it with any
	 * errors.
	 *
	 * @param arg
	 * @param validator
	 * @param errors
	 * @return
	 */
	private void validateClassWithErrorTagging(final Object arg,
			final Validator validator, final List<Errors> errors)
			throws Exception {
		final BindException objErrors = new BindException(arg, arg.getClass()
				.getName());
		validator.validate(arg, objErrors);
		if (objErrors.hasErrors()) {
			errors.add(objErrors);
		}
	}

	/**
	 * Takes a single argument and validates it. Also introspect's the object
	 * and digs deeper if necessary.
	 *
	 * @param errors
	 * @param arg
	 * @throws Exception
	 */
	private void validateArgument(final List<Errors> errors, final Object arg)
			throws Exception {
		for (final Validator validator : getValidators()) {
			if (validator.supports(arg.getClass())) {
				logger.debug("[DOMAIN] " + validator.getClass()
						+ " can validate "
						+ arg.getClass());
				validateClass(errors, arg, validator);
			} else {
				// logger.debug("%% "
				// + arg.getClass()
				// +
				// " is not a known domain Object and as such is not currently validatable");
			}
		}
	}

	/**
	 * Deeply validates the specified class using introspection.
	 *
	 * It firstly does a top level validation, and then digs in.
	 *
	 * @param errors
	 * @param arg
	 * @param validator
	 * @throws Exception
	 */
	private void validateClass(final List<Errors> errors, final Object arg,
			final Validator validator) throws Exception {
		/**
		 * 1. Validate the class using the found validator.
		 */
		validateClassWithErrorTagging(arg, validator, errors);
		/**
		 * 2. Introspect the object and recursively call validateClass - tagging
		 * as you go.
		 */
		introspectArgument(arg, errors);
	}

	/**
	 * An Object inspector using AC BeanUtils.
	 *
	 * @param arg
	 * @param errors
	 * @throws Exception
	 */
	private void introspectArgument(final Object arg,
			final List<Errors> errors) throws Exception {

		final PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(arg.getClass());
		for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			final Object propertyValue = propertyDescriptor.getReadMethod()
					.invoke(arg);
			if ((propertyValue != null)
					&& (isClassCollection(propertyValue.getClass()))) {
				validateCollectionArgument(errors, propertyValue);
			} else if (propertyValue != null) {
				validateArgument(errors, propertyValue);
			}
		}
	}

	/**
	 * Validates a Collection argument which requires an additional outer loop
	 * not present in validateArgument.
	 *
	 * @param errors
	 * @param propertyValue
	 * @throws Exception
	 */
	private void validateCollectionArgument(final List<Errors> errors,
			final Object propertyValue) throws Exception {
		for (final Object propertyElementValue : ((Collection<?>) propertyValue)) {
			for (final Validator validator : getValidators()) {
				if (validator.supports(propertyElementValue.getClass())) {
					logger.debug("[COLLECTION] " + validator.getClass()
							+ " can validate the Collection "
							+ propertyElementValue.getClass());
					validateClass(errors, propertyElementValue, validator);
				} else {
					// logger.debug("$$ "
					// + propertyValue.getClass()
					// + " is not validatable as a Collection class right now");
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
	public static boolean isClassCollection(Class<? extends Object> c) {
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
