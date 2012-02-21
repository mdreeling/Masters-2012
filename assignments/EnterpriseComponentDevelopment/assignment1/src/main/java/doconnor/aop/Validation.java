package doconnor.aop;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
@Aspect
public class Validation {
	 @Autowired
	 private List<Validator> validators = new ArrayList<Validator>();


}
