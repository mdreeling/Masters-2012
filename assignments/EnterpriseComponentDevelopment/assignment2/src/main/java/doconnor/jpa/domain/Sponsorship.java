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
public class Sponsorship {

	   @Embeddable
	   public static class Id implements  Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
			@Column
			private Long clubId;
			@Column
			private Long companyId;

	        public Id() {}

	        public Id(Long clubId, Long companyId)  {
				this.clubId = clubId;
				this.companyId = companyId;
			}

			@Override
			public boolean equals(Object o) {
				if (o instanceof Id) {
					Id that = (Id)o;
					return this.clubId.equals(that.clubId) &&
						   this.companyId.equals(that.companyId);
				} else {
					return false;
				}
			}

			@Override
			public int hashCode() {
				return clubId.hashCode() + companyId.hashCode();
			}
		}
		@EmbeddedId
		private Id id;
		@ManyToOne
		@JoinColumn(name="clubId", insertable = false, updatable=false)
		private Club club ;
		@ManyToOne
		@JoinColumn(name="companyId", insertable = false, updatable=false)
		private Company company ;
		private float value ;
		private int duration ;
		private Date startDate ;

		public Sponsorship() {}

		public Sponsorship(Club club, Company company,
				float value, int duration) {
			this.club = club;
			this.company = company;
			this.value = value;
			this.duration = duration;
			this.id = new Id(club.getId(), company.getId()) ;
		}

		public Id getId() {
			return id;
		}

		public void setId(Id id) {
			this.id = id;
		}

		public Club getClub() {
			return club;
		}

		public void setClub(Club club) {
			this.club = club;
		}

		public Company getCompany() {
			return company;
		}

		public void setCompany(Company company) {
			this.company = company;
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
