package doconnor.jpa.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class LicensingDeal {

	@Embeddable
	public static class Id implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		@Column
		private Long playerId;
		@Column
		private Long productId;

		public Id() {
		}

		public Id(Long playerId, Long productId) {
			this.playerId = playerId;
			this.productId = productId;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Id) {
				Id that = (Id) o;
				return this.playerId.equals(that.playerId)
						&& this.productId.equals(that.productId);
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return playerId.hashCode() + productId.hashCode();
		}
	}

	@EmbeddedId
	private Id id;
	@ManyToOne
	@JoinColumn(name = "playerId", insertable = false, updatable = false)
	private Player player;
	@ManyToOne
	@JoinColumn(name = "productId", insertable = false, updatable = false)
	private Product product;

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product
	 *            the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	private float value;
	private int duration;
	private Date startDate;

	public LicensingDeal() {
	}

	public LicensingDeal(Product product, Player player, float value,
			int duration) {
		this.player = player;
		this.value = value;
		this.duration = duration;
		this.id = new Id(player.getId(), product.getId());
		this.product = product;
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
