/*
 * 
 */
package com.dreeling.applications.travel.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * A Hotel Booking made by a User.
 */
@Entity
public class Booking implements Serializable {

	/** The id. */
	private Long id;

	/** The user. */
	private User user;

	/** The hotel. */
	private Hotel hotel;

	/** The checkin date. */
	@DateTimeFormat(pattern = "MM-dd-yyyy")
	private Date checkinDate;

	/** The checkout date. */
	@DateTimeFormat(pattern = "MM-dd-yyyy")
	private Date checkoutDate;

	/** The credit card. */
	private String creditCard;

	/** The credit card name. */
	private String creditCardName;

	/** The credit card expiry month. */
	private int creditCardExpiryMonth;

	/** The credit card expiry year. */
	private int creditCardExpiryYear;

	/** The smoking. */
	private boolean smoking;

	/** The beds. */
	private int beds;

	/** The amenities. */
	private Set<Amenity> amenities;

	/**
	 * Instantiates a new booking.
	 */
	public Booking() {
		Calendar calendar = Calendar.getInstance();
		setCheckinDate(calendar.getTime());
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		setCheckoutDate(calendar.getTime());
	}

	/**
	 * Instantiates a new booking.
	 * 
	 * @param hotel
	 *            the hotel
	 * @param user
	 *            the user
	 */
	public Booking(Hotel hotel, User user) {
		this();
		this.hotel = hotel;
		this.user = user;
	}

	/**
	 * Gets the total.
	 * 
	 * @return the total
	 */
	@Transient
	public BigDecimal getTotal() {
		return hotel.getPrice().multiply(new BigDecimal(getNights()));
	}

	/**
	 * Gets the nights.
	 * 
	 * @return the nights
	 */
	@Transient
	public int getNights() {
		if ((checkinDate == null) || (checkoutDate == null)) {
			return 0;
		} else {
			return (int) (checkoutDate.getTime() - checkinDate.getTime()) / 1000 / 60 / 60 / 24;
		}
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the checkin date.
	 * 
	 * @return the checkin date
	 */
	@Basic
	@Temporal(TemporalType.DATE)
	public Date getCheckinDate() {
		return checkinDate;
	}

	/**
	 * Sets the checkin date.
	 * 
	 * @param datetime
	 *            the new checkin date
	 */
	public void setCheckinDate(Date datetime) {
		this.checkinDate = datetime;
	}

	/**
	 * Gets the hotel.
	 * 
	 * @return the hotel
	 */
	@ManyToOne
	public Hotel getHotel() {
		return hotel;
	}

	/**
	 * Sets the hotel.
	 * 
	 * @param hotel
	 *            the new hotel
	 */
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	@ManyToOne
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 * 
	 * @param user
	 *            the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the checkout date.
	 * 
	 * @return the checkout date
	 */
	@Basic
	@Temporal(TemporalType.DATE)
	public Date getCheckoutDate() {
		return checkoutDate;
	}

	/**
	 * Sets the checkout date.
	 * 
	 * @param checkoutDate
	 *            the new checkout date
	 */
	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	/**
	 * Gets the credit card.
	 * 
	 * @return the credit card
	 */
	public String getCreditCard() {
		return creditCard;
	}

	/**
	 * Sets the credit card.
	 * 
	 * @param creditCard
	 *            the new credit card
	 */
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	@Transient
	public String getDescription() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		return hotel == null ? null : hotel.getName() + ", " + df.format(getCheckinDate()) + " to "
				+ df.format(getCheckoutDate());
	}

	/**
	 * Checks if is smoking.
	 * 
	 * @return true, if is smoking
	 */
	public boolean isSmoking() {
		return smoking;
	}

	/**
	 * Sets the smoking.
	 * 
	 * @param smoking
	 *            the new smoking
	 */
	public void setSmoking(boolean smoking) {
		this.smoking = smoking;
	}

	/**
	 * Gets the beds.
	 * 
	 * @return the beds
	 */
	public int getBeds() {
		return beds;
	}

	/**
	 * Sets the beds.
	 * 
	 * @param beds
	 *            the new beds
	 */
	public void setBeds(int beds) {
		this.beds = beds;
	}

	/**
	 * Gets the credit card name.
	 * 
	 * @return the credit card name
	 */
	public String getCreditCardName() {
		return creditCardName;
	}

	/**
	 * Sets the credit card name.
	 * 
	 * @param creditCardName
	 *            the new credit card name
	 */
	public void setCreditCardName(String creditCardName) {
		this.creditCardName = creditCardName;
	}

	/**
	 * Gets the credit card expiry month.
	 * 
	 * @return the credit card expiry month
	 */
	public int getCreditCardExpiryMonth() {
		return creditCardExpiryMonth;
	}

	/**
	 * Sets the credit card expiry month.
	 * 
	 * @param creditCardExpiryMonth
	 *            the new credit card expiry month
	 */
	public void setCreditCardExpiryMonth(int creditCardExpiryMonth) {
		this.creditCardExpiryMonth = creditCardExpiryMonth;
	}

	/**
	 * Gets the credit card expiry year.
	 * 
	 * @return the credit card expiry year
	 */
	public int getCreditCardExpiryYear() {
		return creditCardExpiryYear;
	}

	/**
	 * Sets the credit card expiry year.
	 * 
	 * @param creditCardExpiryYear
	 *            the new credit card expiry year
	 */
	public void setCreditCardExpiryYear(int creditCardExpiryYear) {
		this.creditCardExpiryYear = creditCardExpiryYear;
	}

	/**
	 * Gets the amenities.
	 * 
	 * @return the amenities
	 */
	@Transient
	public Set<Amenity> getAmenities() {
		return amenities;
	}

	/**
	 * Sets the amenities.
	 * 
	 * @param amenities
	 *            the new amenities
	 */
	public void setAmenities(Set<Amenity> amenities) {
		this.amenities = amenities;
	}

	// TODO replace with JSR 303
	/**
	 * Validate enter booking details.
	 * 
	 * @param context
	 *            the context
	 */
	public void validateEnterBookingDetails(ValidationContext context) {
		MessageContext messages = context.getMessageContext();
		if (checkinDate.before(today())) {
			messages.addMessage(new MessageBuilder().error().source("checkinDate")
					.code("booking.checkinDate.beforeToday").build());
		} else if (checkoutDate.before(checkinDate)) {
			messages.addMessage(new MessageBuilder().error().source("checkoutDate")
					.code("booking.checkoutDate.beforeCheckinDate").build());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Booking(" + user + "," + hotel + ")";
	}

}
