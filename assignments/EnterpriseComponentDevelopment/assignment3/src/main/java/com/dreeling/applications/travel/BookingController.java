package com.dreeling.applications.travel;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/booking")
@SessionAttributes("booking")
public class BookingController {
	private BookingService bookingService;
	private BookingValidator bookingValidator ;

	@Autowired
	public BookingController(BookingService bookingService,
			BookingValidator bookingValidator) {
		this.bookingService = bookingService;
		this.bookingValidator = bookingValidator ;
	}

	@ModelAttribute("hotels")
	public List<Hotel> populateHotels() {
		SearchCriteria sc = new SearchCriteria() ;
		sc.setPageSize(100) ;
	     return bookingService.findHotels(sc);
	 }	
	
	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(ModelMap model,
			@RequestParam( value = "hotelId") Long hid 
			) {
		Hotel hotel = bookingService.findHotelById(hid) ;
		Booking command = new Booking() ;
		command.setHotel(hotel) ;
        model.addAttribute("booking", command);
        return "enterBookingDetails";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit( Principal principal,
	            @ModelAttribute("booking") Booking command ,
	            BindingResult result, SessionStatus status) {
		bookingValidator.validate(command, result) ;
		if (result.hasErrors()) {
			  return "enterBookingDetails";
		} else {
			System.out.println (
					"logged in = " + 
					principal.getName());
		   bookingService.createBooking(command,principal.getName()) ;
		   status.setComplete() ;
		   return "redirect:/hotels" ;
		}
	}
	  
}
