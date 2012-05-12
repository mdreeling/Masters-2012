<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="bookings" "class="section">
<security:authorize ifAllGranted="ROLE_ADMIN">
	<h2>Current Hotel Bookings</h2>

	<c:if test="${empty bookings}">
	<tr>
		<td colspan="7">No bookings found</td>
	</tr>
	</c:if>

	<c:if test="${!empty bookings}">
	<table class="summary">
		<thead>
			<tr>
				<th>Customer</th>
				<th>Hotel</th>
				<th>Address</th>
				<th>City, State</th>
				<th>Check in Date</th>
				<th>Check out Date</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="booking" items="${bookings}">
			<tr>
				<td>${booking.user.name}</td>
				<td>${booking.hotel.name}</td>				
				<td>${booking.hotel.address}</td>
				<td>${booking.hotel.city}, ${booking.hotel.state}</td>
				<td>${booking.checkinDate}</td>
				<td>${booking.checkoutDate}</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	</c:if>
</security:authorize>

</div>