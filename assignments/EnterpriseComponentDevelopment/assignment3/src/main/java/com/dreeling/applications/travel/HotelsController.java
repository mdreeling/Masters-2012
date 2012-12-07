/*
 * 
 */
package com.dreeling.applications.travel;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dreeling.applications.travel.domain.Booking;
import com.dreeling.applications.travel.domain.Hotel;
import com.dreeling.applications.travel.service.BookingService;

/**
 * The Class HotelsController.
 */
@Controller
public class HotelsController {

	/** The booking service. */
	private final BookingService bookingService;

	/**
	 * Instantiates a new hotels controller.
	 * 
	 * @param bookingService
	 *            the booking service
	 */
	@Autowired
	public HotelsController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	/**
	 * Search.
	 * 
	 * @param searchCriteria
	 *            the search criteria
	 * @param currentUser
	 *            the current user
	 * @param model
	 *            the model
	 */
	@RequestMapping(value = "/hotels/search", method = RequestMethod.GET)
	public void search(SearchCriteria searchCriteria, Principal currentUser, Model model) {
		if (currentUser != null) {
			List<Booking> booking = bookingService.findBookings(currentUser.getName());
			model.addAttribute(booking);
		}
	}

	/**
	 * List.
	 * 
	 * @param criteria
	 *            the criteria
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/hotels", method = RequestMethod.GET)
	public String list(SearchCriteria criteria, Model model) {
		List<Hotel> hotels = bookingService.findHotels(criteria);
		model.addAttribute(hotels);
		return "hotels/list";
	}

	/**
	 * Show.
	 * 
	 * @param id
	 *            the id
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/hotels/{id}", method = RequestMethod.GET)
	public String show(@PathVariable Long id, Model model) {
		model.addAttribute(bookingService.findHotelById(id));
		return "hotels/show";
	}

	/**
	 * Show bookings.
	 * 
	 * @param id
	 *            the id
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/hotels/bookings/{id}", method = RequestMethod.GET)
	public String showBookings(@PathVariable Long id, Model model) {
		model.addAttribute("bookings", bookingService.findHotelBookings(id));
		return "hotels/bookings";
	}

	/**
	 * Delete booking.
	 * 
	 * @param id
	 *            the id
	 * @return the string
	 */
	@RequestMapping(value = "/bookings/{id}", method = RequestMethod.DELETE)
	public String deleteBooking(@PathVariable Long id) {
		bookingService.cancelBooking(id);
		return "redirect:../hotels/search";
	}

}