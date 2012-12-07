/*
 *
 */
package com.dreeling.applications.travel.validation;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dreeling.applications.travel.domain.Booking;

/**
 * The Class BookingValidator.
 */
@Component
public class BookingValidator implements Validator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Booking.class.isAssignableFrom(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		Booking bk = (Booking) target;
		if (bk.getCheckinDate().before(today())) {
			errors.reject("invalid.checkinDate", "Invalid checkin date.");
		} else if (bk.getCheckoutDate().before(bk.getCheckinDate())) {
			errors.reject("booking.checkoutDate.beforeCheckinDate", "Invalid checkin date.");
		}
	}

	/**
	 * Today.
	 * 
	 * @return the date
	 */
	private Date today() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}

}
