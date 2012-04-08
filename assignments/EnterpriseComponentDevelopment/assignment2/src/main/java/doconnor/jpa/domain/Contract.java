package doconnor.jpa.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Contract {

	   @Embeddable
	   public static class Id implements  Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
			@Column
			private Long playerId;
			@Column
			private Long agentId;

	        public Id() {}

	        public Id(Long agentId, Long playerId)  {
				this.agentId = agentId;
				this.playerId = playerId;
			}

			@Override
			public boolean equals(Object o) {
				if (o instanceof Id) {
					Id that = (Id)o;
					return this.agentId.equals(that.agentId) &&
						   this.playerId.equals(that.playerId);
				} else {
					return false;
				}
			}

			@Override
			public int hashCode() {
				return agentId.hashCode() + playerId.hashCode();
			}
		}
		@EmbeddedId
		private Id id;
		@ManyToOne
		@JoinColumn(name="agentId", insertable = false, updatable=false)
		private Agent agent ;
		@ManyToOne
		@JoinColumn(name="player", unique = true, updatable=false)
		private Player player ;
		private float value ;

		public Contract() {}

		public Contract(Agent agent, Player player,
				float value) {
			this.agent = agent;
			this.player = player;
			this.value = value;
			this.id = new Id(agent.getId(), player.getId()) ;
		}

		public Id getId() {
			return id;
		}

		public void setId(Id id) {
			this.id = id;
		}

		public Agent getAgent() {
			return agent;
		}

		public void setAgent(Agent agent) {
			this.agent = agent;
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
}
