package doconnor.aop.domain.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import doconnor.aop.domain.Club;

@Component
public class ClubValidator implements Validator {

	public void validate(Object obj, Errors errors) {

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name",
				"rejectIfEmptyOrWhitespace");


		if (((Club) obj).getId() < 0) {
			errors.rejectValue("id", "clubid.negative", "Club id must be > 0");
		}

	}

	@SuppressWarnings("unchecked")
	public boolean supports(final Class clazz) {
		return clazz.isAssignableFrom(Club.class);
	}

}
