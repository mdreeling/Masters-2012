package com.dreeling.applications.travel.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.dreeling.applications.travel.SearchCriteria;
import com.dreeling.applications.travel.domain.Booking;
import com.dreeling.applications.travel.domain.Hotel;
import com.dreeling.applications.travel.domain.User;
import com.dreeling.applications.travel.service.BookingService;

/**
 * A JPA-based implementation of the Booking Service. Delegates to a JPA entity
 * manager to issue data access calls against the backing repository. The
 * EntityManager reference is provided by the managing container (Spring)
 * automatically.
 */
@Service("bookingService")
@Repository
public class JPABookingService implements BookingService {

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<Booking> findBookings(String username) {

		System.out.println("JPABookingService - findBookings... em = " + em);

		if (username != null) {
			return em
					.createQuery(
							"select b from Booking b where b.user.username = :username order by b.checkinDate")
					.setParameter("username", username).getResultList();
		} else {
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<Hotel> findHotels(SearchCriteria criteria) {
		String pattern = getSearchPattern(criteria);
		return em
				.createQuery(
						"select h from Hotel h where lower(h.name) like "
								+ pattern + " or lower(h.city) like " + pattern
								+ " or lower(h.zip) like " + pattern
								+ " or lower(h.address) like " + pattern)
				.setMaxResults(criteria.getPageSize())
				.setFirstResult(criteria.getPage() * criteria.getPageSize())
				.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Booking> findHotelBookings(Long id) {
		Hotel hotel = this.findHotelById(id);
		List<Booking> result = em
				.createQuery("from Booking b where b.hotel = :hotel")
				.setParameter("hotel", hotel).getResultList();
		System.out.println("No. of bookings = " + result.size());
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Hotel findHotelById(Long id) {
		return em.find(Hotel.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public Booking createBooking(Long hotelId, String username) {
		Hotel hotel = em.find(Hotel.class, hotelId);
		User user = findUser(username);
		Booking booking = new Booking(hotel, user);
		em.persist(booking);
		return booking;
	}

	@Override
	@Transactional
	public boolean createBooking(Booking booking, String name) {
		User user = findUser(name);
		// booking.getHotel().setId(hotels.get(0).getId()) ;
		booking.setUser(user);
		em.persist(booking);
		return true;
	}

	@Override
	@Transactional
	public void cancelBooking(Long id) {
		Booking booking = em.find(Booking.class, id);
		if (booking != null) {
			em.remove(booking);
		}
	}

	// helpers

	private String getSearchPattern(SearchCriteria criteria) {
		if (StringUtils.hasText(criteria.getSearchString())) {
			return "'%"
					+ criteria.getSearchString().toLowerCase()
							.replace('*', '%') + "%'";
		} else {
			return "'%'";
		}
	}

	private User findUser(String username) {
		return (User) em
				.createQuery(
						"select u from User u where u.username = :username")
				.setParameter("username", username).getSingleResult();
	}

}