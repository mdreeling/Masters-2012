/*
 * 
 */
package com.dreeling.applications.travel.service;

import java.util.List;

import com.dreeling.applications.travel.SearchCriteria;
import com.dreeling.applications.travel.domain.Booking;
import com.dreeling.applications.travel.domain.Hotel;

/**
 * A service interface for retrieving hotels and bookings from a backing
 * repository. Also supports the ability to cancel a booking.
 */
public interface BookingService {

	/**
	 * Find bookings made by the given user.
	 * 
	 * @param username
	 *            the user's name
	 * @return their bookings
	 */
	public List<Booking> findBookings(String username);

	/**
	 * Find hotels available for booking by some criteria.
	 * 
	 * @param criteria
	 *            the search criteria
	 * @return a list of hotels meeting the criteria
	 */
	public List<Hotel> findHotels(SearchCriteria criteria);

	/**
	 * Find hotels by their identifier.
	 * 
	 * @param id
	 *            the hotel id
	 * @return the hotel
	 */
	public Hotel findHotelById(Long id);

	/**
	 * Create a new, transient hotel booking instance for the given user.
	 * 
	 * @param hotelId
	 *            the hotelId
	 * @param userName
	 *            the user name
	 * @return the new transient booking instance
	 */
	public Booking createBooking(Long hotelId, String userName);

	/**
	 * Creates the booking.
	 * 
	 * @param booking
	 *            the booking
	 * @param name
	 *            the name
	 * @return true, if successful
	 */
	public boolean createBooking(Booking booking, String name);

	/**
	 * Cancel an existing booking.
	 * 
	 * @param id
	 *            the booking id
	 */
	public void cancelBooking(Long id);

	/**
	 * Find hotel bookings.
	 * 
	 * @param id
	 *            the id
	 * @return the list
	 */
	public List<Booking> findHotelBookings(Long id);

}
