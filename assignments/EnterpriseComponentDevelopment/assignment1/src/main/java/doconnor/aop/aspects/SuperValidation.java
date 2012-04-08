package doconnor.aop.aspects;

import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

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

import doconnor.aop.domain.validation.ClubValidator;

/**
 *
 * @author Neo
 *
 */
@Aspect
public class SuperValidation {
	@Autowired
	private List<Validator> validators = new ArrayList<Validator>();

	Properties properties = new Properties();

	private final Log log = LogFactory.getLog(getClass());

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

		final List<Errors> errors = new ArrayList<Errors>();

		for (Object arg : joinPoint.getArgs()) {
			validate(errors, arg);
		}

		joinPoint.proceed();

		log.debug("Method validation complete");
	}

	private void validate(Object obj) {
		System.out.println("Validating " + obj.getClass());
		Errors errors = new BindException(obj, obj.getClass().getName());

		// At this point i would like to retrieve my Validator for the object -
		// trying to think generic.
		// I guess i could use a factory.. but then
		// validator.supports(obj.getClass() seems dumb (i'll know by then)
		Validator validator = new ClubValidator(); // This is currently
													// referencing 1 of many
													// validators i.e
													// PlayerValidator

		// ValidationUtils.invokeValidator(this.addressValidator, obj, errors);

		if (validator.supports(obj.getClass())) {
			validator.validate(obj, errors);
		}
		if (errors.hasFieldErrors()) {
			System.out.println(errors.getFieldErrorCount());
		}
	}

	/**
	 * An Object inspector using AC BeanUtils.
	 *
	 * @param arg
	 * @param errors
	 * @throws Exception
	 */
	private void inspectObjectProperties(final Object arg,
			final List<Errors> errors) throws Exception {

		final PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(arg.getClass());
		for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			final Object propertyValue = propertyDescriptor.getReadMethod()
					.invoke(arg);
			if ((propertyValue != null)
					&& isArrayOrCollection(propertyValue.getClass())) {
				if (propertyValue.getClass().isArray()) {
					for (final Object propertyElementValue : ((Object[]) propertyValue)) {
						for (final Validator validator : getValidators()) {
							if (validator.supports(propertyElementValue
									.getClass())) {
								log.debug("Validator supported: "
										+ propertyElementValue.getClass());
								validateAndAddErrors(propertyElementValue,
										validator, errors);
								inspectObjectProperties(propertyElementValue,
										errors);
							}
						}
					}
				} else {
					for (final Object propertyElementValue : ((Collection) propertyValue)) {
						for (final Validator validator : getValidators()) {
							if (validator.supports(propertyElementValue
									.getClass())) {
								log.debug("Validator supported: "
										+ propertyElementValue.getClass());
								validateAndAddErrors(propertyElementValue,
										validator, errors);
								inspectObjectProperties(propertyElementValue,
										errors);
							}
						}
					}
				}
			} else if (propertyValue != null) {
				// Non-Scalar property
				for (final Validator validator : getValidators()) {
					if (validator.supports(propertyValue.getClass())) {
						log.debug("Validator supported: "
								+ propertyValue.getClass());
						validateAndAddErrors(propertyValue, validator, errors);
						inspectObjectProperties(propertyValue, errors);
					}
				}
			}
		}
	}

	private Errors validateAndAddErrors(final Object arg,
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
				log.debug("Validator supported: " + arg.getClass());
				validateAndAddErrors(arg, validator, errors);
				inspectObjectProperties(arg, errors);
			}
		}
	}

	private boolean isArrayOrCollection(final Class clazz) {
		return (clazz.isArray() || clazz.isAssignableFrom(List.class)
				|| clazz.isAssignableFrom(ArrayList.class)
				|| clazz.isAssignableFrom(Set.class)
				|| clazz.isAssignableFrom(SortedSet.class)
				|| clazz.isAssignableFrom(AbstractCollection.class)
				|| clazz.isAssignableFrom(AbstractList.class)
				|| clazz.isAssignableFrom(AbstractSet.class)
				|| clazz.isAssignableFrom(HashSet.class)
				|| clazz.isAssignableFrom(LinkedHashSet.class)
				|| clazz.isAssignableFrom(LinkedList.class)
				|| clazz.isAssignableFrom(TreeSet.class) || clazz
					.isAssignableFrom(Vector.class));
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
