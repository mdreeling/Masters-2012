package doconnor.aop.domain.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LongValidator implements Validator {

	public void validate(Object obj, Errors errors) {
		if (((Long) obj) < 0) {
			errors.reject("All long ids must be > 0");
		}

	}

	@SuppressWarnings("unchecked")
	public boolean supports(final Class clazz) {
		return clazz.isAssignableFrom(Long.class);
	}

}
