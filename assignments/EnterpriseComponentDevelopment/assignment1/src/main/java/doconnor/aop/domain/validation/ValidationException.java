package doconnor.aop.domain.validation;

import java.util.List;

import org.springframework.validation.Errors;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 2605997256235741510L;
	private List<Errors> errors;

	public ValidationException(final List<Errors> errors) {
		this.errors = errors;
	}

	public List<Errors> getErrors() {
		return errors;
	}

	public void setErrors(final List<Errors> errors) {
		this.errors = errors;
	}

}
