/*
 * 
 */
package com.dreeling.applications.travel;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.dreeling.applications.travel.domain.Booking;
import com.dreeling.applications.travel.domain.Hotel;
import com.dreeling.applications.travel.service.BookingService;
import com.dreeling.applications.travel.validation.BookingValidator;

/**
 * The Class BookingController.
 */
@Controller
@RequestMapping("/booking")
@SessionAttributes("booking")
public class BookingController {

	/** The booking service. */
	private final BookingService bookingService;

	/** The booking validator. */
	private final BookingValidator bookingValidator;

	/**
	 * Instantiates a new booking controller.
	 * 
	 * @param bookingService
	 *            the booking service
	 * @param bookingValidator
	 *            the booking validator
	 */
	@Autowired
	public BookingController(BookingService bookingService, BookingValidator bookingValidator) {
		this.bookingService = bookingService;
		this.bookingValidator = bookingValidator;
	}

	/**
	 * Populate hotels.
	 * 
	 * @return the list
	 */
	@ModelAttribute("hotels")
	public List<Hotel> populateHotels() {
		SearchCriteria sc = new SearchCriteria();
		sc.setPageSize(100);
		return bookingService.findHotels(sc);
	}

	/**
	 * Setup form.
	 * 
	 * @param model
	 *            the model
	 * @param hid
	 *            the hid
	 * @return the string
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(ModelMap model, @RequestParam(value = "hotelId") Long hid) {
		Hotel hotel = bookingService.findHotelById(hid);
		Booking command = new Booking();
		command.setHotel(hotel);
		model.addAttribute("booking", command);
		return "enterBookingDetails";
	}

	/**
	 * Process submit.
	 * 
	 * @param principal
	 *            the principal
	 * @param command
	 *            the command
	 * @param result
	 *            the result
	 * @param status
	 *            the status
	 * @return the string
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit(Principal principal, @ModelAttribute("booking") Booking command, BindingResult result,
			SessionStatus status) {
		bookingValidator.validate(command, result);
		if (result.hasErrors()) {
			return "enterBookingDetails";
		} else {
			System.out.println("logged in = " + principal.getName());
			bookingService.createBooking(command, principal.getName());
			status.setComplete();
			return "redirect:/hotels";
		}
	}

}
