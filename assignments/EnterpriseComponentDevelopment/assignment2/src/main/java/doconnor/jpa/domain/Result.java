package doconnor.jpa.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Result {
	@Id
	@GeneratedValue
	private long id;
	@ManyToOne
	@JoinColumn(name = "homeclubId", nullable = false)
	private Club homeClub;
	@ManyToOne
	@JoinColumn(name = "awayclubId", nullable = false)
	private Club awayClub;
	private int homeScore;
	private int awayScore;
	private Date datePlayed;

	/**
	 * @return the datePlayed
	 */
	public Date getDatePlayed() {
		return datePlayed;
	}

	/**
	 * @param datePlayed
	 *            the datePlayed to set
	 */
	public void setDatePlayed(Date datePlayed) {
		this.datePlayed = datePlayed;
	}

	public Result(Date date, Club homeClub, Club awayClub, int homeScore,
			int awayScore) {
		super();
		this.datePlayed = date;
		this.homeClub = homeClub;
		this.awayClub = awayClub;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
	}

	public Result() {
		super();
	}

	/**
	 * @return the awayClub
	 */
	public Club getAwayClub() {
		return awayClub;
	}

	/**
	 * @param awayClub
	 *            the awayClub to set
	 */
	public void setAwayClub(Club awayClub) {
		this.awayClub = awayClub;
	}

	/**
	 * @return the homeScore
	 */
	public int getHomeScore() {
		return homeScore;
	}

	/**
	 * @param homeScore
	 *            the homeScore to set
	 */
	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	/**
	 * @return the awayScore
	 */
	public int getAwayScore() {
		return awayScore;
	}

	/**
	 * @param awayScore
	 *            the awayScore to set
	 */
	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	/**
	 * @return the homeClub
	 */
	public Club getHomeClub() {
		return homeClub;
	}

	/**
	 * @param homeClub
	 *            the homeClub to set
	 */
	public void setHomeClub(Club homeClub) {
		this.homeClub = homeClub;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
}
