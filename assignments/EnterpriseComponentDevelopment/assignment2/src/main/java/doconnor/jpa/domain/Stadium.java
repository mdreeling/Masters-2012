package doconnor.jpa.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Stadium {
	String title ;
	int capacity ;
	float ticket_price ;
	
	public Stadium() { }
	
	public Stadium(String title, int capacity, float ticket_price) {
		super();
		this.title = title;
		this.capacity = capacity;
		this.ticket_price = ticket_price;
	}

	public int getCapacity() {
		return capacity;
	}
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public float getTicket_price() {
		return ticket_price;
	}
	public void setTicket_price(float ticket_price) {
		this.ticket_price = ticket_price;
	}
	
}
