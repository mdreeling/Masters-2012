package doconnor.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ClubStats {

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	@JoinColumn(name = "club", unique = true, updatable = false)
	private Club club;

	private float won;

	private float draw;

	private float lost;

	private float points;

	public ClubStats() {
	}

	public ClubStats(Club club, float won, float draw, float lost, float points) {
		super();
		this.club = club;
		this.won = won;
		this.draw = draw;
		this.lost = lost;
		this.points = points;
	}

	/**
	 * @return the club
	 */
	public Club getClub() {
		return club;
	}

	/**
	 * @return the draw
	 */
	public float getDraw() {
		return draw;
	}

	/**
	 * @return the lost
	 */
	public float getLost() {
		return lost;
	}

	/**
	 * @return the points
	 */
	public float getPoints() {
		return points;
	}

	/**
	 * @return the won
	 */
	public float getWon() {
		return won;
	}

	/**
	 * @param club
	 *            the club to set
	 */
	public void setClub(Club club) {
		this.club = club;
	}
	/**
	 * @param draw
	 *            the draw to set
	 */
	public void setDraw(float draw) {
		this.draw = draw;
	}

	/**
	 * @param lost
	 *            the lost to set
	 */
	public void setLost(float lost) {
		this.lost = lost;
	}

	/**
	 * @param points
	 *            the points to set
	 */
	public void setPoints(float points) {
		this.points = points;
	}

	/**
	 * @param won
	 *            the won to set
	 */
	public void setWon(float won) {
		this.won = won;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}


}
